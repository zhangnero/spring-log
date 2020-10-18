package com.nero.spring.log.config.wrapper;

import javax.servlet.ServletOutputStream;
import javax.servlet.WriteListener;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;
import java.io.*;

public class ResponseBodyReadWrapper extends HttpServletResponseWrapper {
    private ServletOutputStream outputStream;
    private PrintWriter printWriter;
    private ServletOutputStreamBackup backup;

    /**
     * Constructs a response adaptor wrapping the given response.
     *
     * @param response The response to be wrapped
     * @throws IllegalArgumentException if the response is null
     */
    public ResponseBodyReadWrapper(HttpServletResponse response) throws IOException {
        super(response);
    }

    @Override
    public void setContentType(String type) {
        super.setContentType(type);
        try {
            ((ServletOutputStreamBackup)getOutputStream()).setNeedBackUp(type == null || type.contains("application/json"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public ServletOutputStream getOutputStream() throws IOException {
        if (printWriter != null) {
            throw new IllegalStateException("getWriter() has already been called on this response.");
        }

        if (outputStream == null) {
            outputStream = getResponse().getOutputStream();
            backup = new ServletOutputStreamBackup(outputStream);
        }

        return backup;
    }

    @Override
    public PrintWriter getWriter() throws IOException {
        if (outputStream != null) {
            throw new IllegalStateException("getOutputStream() has already been called on this response.");
        }

        if (printWriter == null) {
            backup = new ServletOutputStreamBackup(getResponse().getOutputStream());
            printWriter = new PrintWriter(new OutputStreamWriter(backup, getResponse().getCharacterEncoding()), true);
        }

        return printWriter;
    }

    @Override
    public void flushBuffer() throws IOException {

        if (printWriter != null){
            printWriter.flush();
        }
        if (outputStream != null){
            outputStream.flush();
        }
    }

    public byte[] getBackup() {
        if (backup != null) {
            return backup.getBackup();
        } else {
            return new byte[0];
        }
    }

    private static class ServletOutputStreamBackup extends ServletOutputStream{

        private boolean needBackUp = true;

        private OutputStream outputStream;

        private ByteArrayOutputStream buckup;

        public ServletOutputStreamBackup(OutputStream outputStream) {
            this.outputStream = outputStream;
            buckup = new ByteArrayOutputStream(1024);
        }

        @Override
        public void write(int b) throws IOException {
            outputStream.write(b);
            if (needBackUp){
                buckup.write(b);
            }
        }

        @Override
        public boolean isReady() {
            return false;
        }

        @Override
        public void setWriteListener(WriteListener listener) {

        }

        public byte[] getBackup(){
            return buckup.toByteArray();
        }

        public void setNeedBackUp(boolean needBackUp) {
            this.needBackUp = needBackUp;
        }
    }
}
