package com.upsight.mediation.vast.processor;

import android.text.TextUtils;
import com.upsight.mediation.vast.model.VASTMediaFile;
import com.upsight.mediation.vast.model.VASTModel;
import java.util.List;

public class VASTModelPostValidator {
    private static final String TAG = "VASTModelPostValidator";

    public static boolean pickMediaFile(VASTModel vASTModel, VASTMediaPicker vASTMediaPicker) {
        if (vASTMediaPicker == null) {
            return false;
        }
        VASTMediaFile pickVideo = vASTMediaPicker.pickVideo(vASTModel.getMediaFiles());
        if (pickVideo == null) {
            return false;
        }
        Object value = pickVideo.getValue();
        String delivery = pickVideo.getDelivery();
        if (TextUtils.isEmpty(value)) {
            return false;
        }
        vASTModel.setPickedMediaFileLocation(value);
        vASTModel.setPickedMediaFileDeliveryType(delivery);
        return true;
    }

    public static boolean validate(VASTModel vASTModel) {
        return validateModel(vASTModel);
    }

    private static boolean validateModel(VASTModel vASTModel) {
        if (!vASTModel.evaluateAdTitle() || !vASTModel.evaluateAdSystem()) {
            return false;
        }
        List impressions = vASTModel.getImpressions();
        if (impressions == null || impressions.size() == 0) {
            return false;
        }
        impressions = vASTModel.getMediaFiles();
        return (impressions == null || impressions.size() == 0) ? false : true;
    }
}
