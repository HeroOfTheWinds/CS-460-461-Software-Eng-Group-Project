package com.upsight.mediation.vast.processor;

import android.content.Context;
import com.upsight.android.googlepushservices.UpsightPushNotificationBuilderFactory.Default;
import com.upsight.mediation.ads.adapters.VastAdAdapter;
import com.upsight.mediation.log.FuseLog;
import com.upsight.mediation.vast.VASTPlayer;
import com.upsight.mediation.vast.model.VASTModel;
import com.upsight.mediation.vast.model.VAST_DOC_ELEMENTS;
import com.upsight.mediation.vast.util.XmlTools;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.nio.charset.Charset;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

public final class VASTProcessor {
    private static final boolean IS_VALIDATION_ON = false;
    private static final int MAX_VAST_LEVELS = 5;
    private static final String TAG = "VASTProcessor";
    private final VASTPlayer mVastPlayer;
    private VASTMediaPicker mediaPicker;
    private StringBuilder mergedVastDocs;
    private VASTModel vastModel;

    public VASTProcessor(VASTMediaPicker vASTMediaPicker, VASTPlayer vASTPlayer) {
        this.mergedVastDocs = new StringBuilder(500);
        this.mediaPicker = vASTMediaPicker;
        this.mVastPlayer = vASTPlayer;
    }

    private Document createDoc(InputStream inputStream) {
        try {
            Document parse = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(inputStream);
            parse.getDocumentElement().normalize();
            return parse;
        } catch (Exception e) {
            return null;
        }
    }

    private void merge(Document document) {
        this.mergedVastDocs.append(XmlTools.xmlDocumentToString(document.getElementsByTagName(VastAdAdapter.NAME).item(0)));
    }

    private int processUri(InputStream inputStream, int i) {
        if (i >= MAX_VAST_LEVELS) {
            return VASTPlayer.ERROR_EXCEEDED_WRAPPER_LIMIT;
        }
        Document createDoc = createDoc(inputStream);
        if (createDoc == null) {
            return 100;
        }
        String str = createDoc.getFirstChild().getNodeName().equals(VastAdAdapter.NAME) ? createDoc.getFirstChild().getAttributes().getNamedItem("version").getNodeValue().toString() : createDoc.getChildNodes().item(1).getAttributes().getNamedItem("version").getNodeValue().toString();
        if (!str.equals("2.0") && !str.equals("3.0")) {
            return VASTPlayer.ERROR_UNSUPPORTED_VERSION;
        }
        NodeList elementsByTagName = createDoc.getElementsByTagName(VAST_DOC_ELEMENTS.vastAdTagURI.getValue());
        if (elementsByTagName == null || elementsByTagName.getLength() == 0) {
            merge(createDoc);
            return 0;
        }
        FuseLog.m239v(TAG, "Doc is a wrapper. ");
        str = XmlTools.getElementValue(elementsByTagName.item(0));
        FuseLog.m239v(TAG, "Wrapper URL: " + str);
        try {
            HttpURLConnection httpURLConnection = (HttpURLConnection) new URL(str).openConnection();
            httpURLConnection.setConnectTimeout(Default.HTTP_REQUEST_TIMEOUT_MS);
            httpURLConnection.setReadTimeout(Default.HTTP_REQUEST_TIMEOUT_MS);
            if (httpURLConnection.getResponseCode() != 200) {
                return VASTPlayer.ERROR_NO_VAST_IN_WRAPPER;
            }
            InputStream inputStream2 = httpURLConnection.getInputStream();
            int processUri = processUri(inputStream2, i + 1);
            try {
                inputStream2.close();
                return processUri;
            } catch (IOException e) {
                return processUri;
            }
        } catch (MalformedURLException e2) {
            return VASTPlayer.ERROR_NO_VAST_IN_WRAPPER;
        } catch (SocketTimeoutException e3) {
            return VASTPlayer.ERROR_WRAPPER_TIMEOUT;
        } catch (Throwable e4) {
            FuseLog.m241w(TAG, e4.getMessage(), e4);
            return VASTPlayer.ERROR_GENERAL_WRAPPER;
        }
    }

    private Document wrapMergedVastDocWithVasts() {
        return XmlTools.stringToDocument(this.mergedVastDocs.toString());
    }

    public VASTModel getModel() {
        return this.vastModel;
    }

    public int process(Context context, String str, boolean z, int i) {
        FuseLog.m235d(TAG, "process");
        this.vastModel = null;
        try {
            InputStream byteArrayInputStream = new ByteArrayInputStream(str.getBytes(Charset.defaultCharset().name()));
            int processUri = processUri(byteArrayInputStream, 0);
            try {
                byteArrayInputStream.close();
            } catch (IOException e) {
            }
            if (processUri != 0) {
                return processUri;
            }
            Document wrapMergedVastDocWithVasts = wrapMergedVastDocWithVasts();
            this.vastModel = new VASTModel(wrapMergedVastDocWithVasts);
            if (wrapMergedVastDocWithVasts == null) {
                return 100;
            }
            if (z && !VASTModelPostValidator.validate(this.vastModel)) {
                return VASTPlayer.ERROR_SCHEMA_VALIDATION;
            }
            if (!VASTModelPostValidator.pickMediaFile(this.vastModel, this.mediaPicker)) {
                return VASTPlayer.ERROR_NO_COMPATIBLE_MEDIA_FILE;
            }
            if (this.vastModel.getPickedMediaFileDeliveryType().equals("progressive")) {
                return this.vastModel.cache(context, this.mVastPlayer, i);
            }
            if (this.vastModel.getPickedMediaFileDeliveryType().equals("streaming")) {
                this.mVastPlayer.setLoaded(true);
            }
            return 0;
        } catch (Throwable e2) {
            FuseLog.m241w(TAG, e2.getMessage(), e2);
            return 100;
        }
    }
}
