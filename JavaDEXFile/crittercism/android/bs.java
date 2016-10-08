package crittercism.android;

import android.content.Context;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

public final class bs {
    public final File f520a;
    public String f521b;
    public List f522c;
    private cj f523d;
    private int f524e;
    private int f525f;
    private int f526g;
    private C1079a f527h;
    private boolean f528i;

    /* renamed from: crittercism.android.bs.a */
    public static final class C1079a {
        int f519a;

        public C1079a(int i) {
            this.f519a = i;
        }
    }

    public bs(Context context, br brVar) {
        this(new File(context.getFilesDir().getAbsolutePath() + "//com.crittercism//" + brVar.m560a()), brVar.m562c(), brVar.m563d(), brVar.m564e(), brVar.m561b(), brVar.m565f());
    }

    private bs(File file, C1079a c1079a, cj cjVar, int i, int i2, String str) {
        this.f528i = false;
        this.f527h = c1079a;
        this.f523d = cjVar;
        this.f526g = i;
        this.f525f = i2;
        this.f521b = str;
        this.f520a = file;
        file.mkdirs();
        m567d();
        this.f524e = m571h().length;
        this.f522c = new LinkedList();
    }

    private boolean m566c(ch chVar) {
        File file = new File(this.f520a, chVar.m490e());
        OutputStream outputStream = null;
        try {
            outputStream = new BufferedOutputStream(new FileOutputStream(file));
        } catch (FileNotFoundException e) {
            new StringBuilder("Could not open output stream to : ").append(file);
            dx.m773a();
        }
        try {
            chVar.m489a(outputStream);
            return true;
        } catch (Throwable e2) {
            file.delete();
            dx.m776a("Unable to write to " + file.getAbsolutePath(), e2);
            return false;
        } finally {
            try {
                outputStream.close();
            } catch (Throwable e22) {
                file.delete();
                dx.m776a("Unable to close " + file.getAbsolutePath(), e22);
                return false;
            }
        }
    }

    private boolean m567d() {
        if (!this.f520a.isDirectory()) {
            this.f528i = true;
            String absolutePath = this.f520a.getAbsolutePath();
            if (this.f520a.exists()) {
                IOException iOException = new IOException(absolutePath + " is not a directory");
            } else {
                FileNotFoundException fileNotFoundException = new FileNotFoundException(absolutePath + " does not exist");
            }
        }
        return !this.f528i;
    }

    private void m568e() {
        while (m578b() > m572i()) {
            if (!m569f()) {
                return;
            }
        }
    }

    private boolean m569f() {
        C1079a c1079a = this.f527h;
        if (this.f527h != null) {
            C1079a c1079a2 = this.f527h;
            File[] g = m570g();
            File file = null;
            if (g.length > c1079a2.f519a) {
                file = g[c1079a2.f519a];
            }
            if (file != null && file.delete()) {
                return true;
            }
        }
        return false;
    }

    private File[] m570g() {
        File[] h = m571h();
        Arrays.sort(h);
        return h;
    }

    private File[] m571h() {
        File[] listFiles = this.f520a.listFiles();
        return listFiles == null ? new File[0] : listFiles;
    }

    private int m572i() {
        int i;
        synchronized (this) {
            i = this.f525f;
        }
        return i;
    }

    public final bs m573a(Context context) {
        return new bs(new File(context.getFilesDir().getAbsolutePath() + "//com.crittercism/pending/" + (this.f520a.getName() + "_" + UUID.randomUUID().toString())), this.f527h, this.f523d, this.f526g, this.f525f, this.f521b);
    }

    public final void m574a() {
        synchronized (this) {
            if (m567d()) {
                File[] h = m571h();
                for (File delete : h) {
                    delete.delete();
                }
            }
        }
    }

    public final void m575a(bs bsVar) {
        if (bsVar != null) {
            int compareTo = this.f520a.getName().compareTo(bsVar.f520a.getName());
            if (compareTo != 0) {
                bs bsVar2;
                bs bsVar3;
                if (compareTo < 0) {
                    bsVar2 = bsVar;
                    bsVar3 = this;
                } else {
                    bsVar2 = this;
                    bsVar3 = bsVar;
                }
                synchronized (r2) {
                    synchronized (r1) {
                        if (m567d() && bsVar.m567d()) {
                            File[] g = m570g();
                            for (compareTo = 0; compareTo < g.length; compareTo++) {
                                g[compareTo].renameTo(new File(bsVar.f520a, g[compareTo].getName()));
                            }
                            bsVar.m568e();
                            for (bt d : this.f522c) {
                                d.m528d();
                            }
                            return;
                        }
                        return;
                    }
                }
            }
        }
    }

    public final void m576a(String str) {
        synchronized (this) {
            if (m567d() && str != null) {
                File file = new File(this.f520a.getAbsolutePath(), str);
                if (file.exists()) {
                    file.delete();
                }
            }
        }
    }

    public final boolean m577a(ch chVar) {
        boolean z = false;
        synchronized (this) {
            if (m567d()) {
                if (this.f524e >= this.f526g) {
                    dx.m778b();
                } else {
                    int b = m578b();
                    if (b != m572i() || m569f()) {
                        if (b > m572i()) {
                            this.f528i = true;
                        } else {
                            boolean c = m566c(chVar);
                            if (c) {
                                this.f524e++;
                            }
                            synchronized (this.f522c) {
                                for (bt c2 : this.f522c) {
                                    c2.m527c();
                                }
                            }
                            z = c;
                        }
                    }
                }
            }
        }
        return z;
    }

    public final int m578b() {
        int length;
        synchronized (this) {
            length = m571h().length;
        }
        return length;
    }

    public final boolean m579b(ch chVar) {
        boolean c;
        synchronized (this) {
            if (m567d()) {
                new File(this.f520a, chVar.m490e()).delete();
                c = m566c(chVar);
            } else {
                c = false;
            }
        }
        return c;
    }

    public final List m580c() {
        List arrayList;
        synchronized (this) {
            arrayList = new ArrayList();
            if (m567d()) {
                cj cjVar = this.f523d;
                File[] g = m570g();
                for (File a : g) {
                    arrayList.add(this.f523d.m652a(a));
                }
            }
        }
        return arrayList;
    }
}
