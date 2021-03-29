package a.tlib.utils.retrofit.converter;

import java.io.File;
import java.io.IOException;

import a.tlib.utils.retrofit.observer.LoadOnSubscribe;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.internal.Util;
import okio.Buffer;
import okio.BufferedSink;
import okio.Okio;
import okio.Source;

/**
 * 上传文件 计算上传进度 请求体
 * Created by fangs on 2018/11/12.
 */
public class FileProgressRequestBody extends RequestBody {

    protected File file;
    protected String contentType;
    protected LoadOnSubscribe subscribe;

    protected FileProgressRequestBody() {}

    public FileProgressRequestBody(File file, String contentType, LoadOnSubscribe subscribe) {
        this.file = file;
        this.contentType = contentType;
        this.subscribe = subscribe;
    }

    @Override
    public long contentLength() {
        return file.length();
    }

    @Override
    public MediaType contentType() {
        return MediaType.parse(contentType);
    }

    public static final int SEGMENT_SIZE = 2048;
    @Override
    public void writeTo(BufferedSink sink) throws IOException {
        boolean ispercent = sink instanceof Buffer;//如果传入的 sink 为 Buffer 类型，则直接写入，不进行百分比统计
        Source source = null;
        try {
            source = Okio.source(file);
            long total = 0;

            long read;
            while ((read = source.read(sink.buffer(), SEGMENT_SIZE)) != -1) {
                total += read;
                sink.flush();

                if (!ispercent ){
                    if (null != subscribe)subscribe.onRead(read);
                }
            }
        } finally {
            Util.closeQuietly(source);
        }
    }

}
