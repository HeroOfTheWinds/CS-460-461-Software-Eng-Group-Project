package org.apache.commons.io.input;

import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;

public class NullInputStream extends InputStream {
    private boolean eof;
    private long mark;
    private final boolean markSupported;
    private long position;
    private long readlimit;
    private final long size;
    private final boolean throwEofException;

    public NullInputStream(long j) {
        this(j, true, false);
    }

    public NullInputStream(long j, boolean z, boolean z2) {
        this.mark = -1;
        this.size = j;
        this.markSupported = z;
        this.throwEofException = z2;
    }

    private int doEndOfFile() throws EOFException {
        this.eof = true;
        if (!this.throwEofException) {
            return -1;
        }
        throw new EOFException();
    }

    public int available() {
        long j = this.size - this.position;
        return j <= 0 ? 0 : j > 2147483647L ? Integer.MAX_VALUE : (int) j;
    }

    public void close() throws IOException {
        this.eof = false;
        this.position = 0;
        this.mark = -1;
    }

    public long getPosition() {
        return this.position;
    }

    public long getSize() {
        return this.size;
    }

    public void mark(int i) {
        synchronized (this) {
            if (this.markSupported) {
                this.mark = this.position;
                this.readlimit = (long) i;
            } else {
                throw new UnsupportedOperationException("Mark not supported");
            }
        }
    }

    public boolean markSupported() {
        return this.markSupported;
    }

    protected int processByte() {
        return 0;
    }

    protected void processBytes(byte[] bArr, int i, int i2) {
    }

    public int read() throws IOException {
        if (this.eof) {
            throw new IOException("Read after end of file");
        } else if (this.position == this.size) {
            return doEndOfFile();
        } else {
            this.position++;
            return processByte();
        }
    }

    public int read(byte[] bArr) throws IOException {
        return read(bArr, 0, bArr.length);
    }

    public int read(byte[] bArr, int i, int i2) throws IOException {
        if (this.eof) {
            throw new IOException("Read after end of file");
        } else if (this.position == this.size) {
            return doEndOfFile();
        } else {
            this.position += (long) i2;
            if (this.position > this.size) {
                i2 -= (int) (this.position - this.size);
                this.position = this.size;
            }
            processBytes(bArr, i, i2);
            return i2;
        }
    }

    public void reset() throws IOException {
        synchronized (this) {
            if (!this.markSupported) {
                throw new UnsupportedOperationException("Mark not supported");
            } else if (this.mark < 0) {
                throw new IOException("No position has been marked");
            } else if (this.position > this.mark + this.readlimit) {
                throw new IOException("Marked position [" + this.mark + "] is no longer valid - passed the read limit [" + this.readlimit + "]");
            } else {
                this.position = this.mark;
                this.eof = false;
            }
        }
    }

    public long skip(long j) throws IOException {
        if (this.eof) {
            throw new IOException("Skip after end of file");
        } else if (this.position == this.size) {
            return (long) doEndOfFile();
        } else {
            this.position += j;
            if (this.position <= this.size) {
                return j;
            }
            long j2 = this.position;
            long j3 = this.size;
            this.position = this.size;
            return j - (j2 - j3);
        }
    }
}
