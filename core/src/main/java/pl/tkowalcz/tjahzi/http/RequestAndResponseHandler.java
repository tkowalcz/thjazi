package pl.tkowalcz.tjahzi.http;

import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpStatusClass;
import io.netty.handler.codec.http.HttpUtil;
import io.netty.util.ReferenceCountUtil;
import pl.tkowalcz.tjahzi.stats.MonitoringModule;

import java.nio.charset.Charset;

@ChannelHandler.Sharable
class RequestAndResponseHandler extends ChannelDuplexHandler {

    private final MonitoringModule monitoringModule;

    RequestAndResponseHandler(MonitoringModule monitoringModule) {
        this.monitoringModule = monitoringModule;
    }

    @Override
    public void write(ChannelHandlerContext ctx, Object object, ChannelPromise promise) throws Exception {
        if (object instanceof FullHttpRequest) {
            int payloadSize = ((FullHttpRequest) object).content().readableBytes();
            monitoringModule.incrementSentHttpRequests(payloadSize);
        }

        super.write(ctx, object, promise);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object object) {
        FullHttpResponse msg = (FullHttpResponse) object;

        monitoringModule.incrementHttpResponses();
        if (msg.status().codeClass() != HttpStatusClass.SUCCESS) {
            System.out.println(msg.content().toString(Charset.defaultCharset()));
            monitoringModule.incrementHttpErrors(
                    msg.status().code(),
                    msg.content().toString(Charset.defaultCharset())
            );
        }

        if (!HttpUtil.isKeepAlive(msg)) {
            ctx.close();
        }

        ReferenceCountUtil.release(msg);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        monitoringModule.addPipelineError(cause);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) {
        monitoringModule.incrementChannelInactive();
    }
}
