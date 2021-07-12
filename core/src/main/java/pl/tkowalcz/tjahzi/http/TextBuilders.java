package pl.tkowalcz.tjahzi.http;

import pl.tkowalcz.tjahzi.utils.TextBuilder;

public class TextBuilders {

    private static final ThreadLocal<TextBuilder> THREAD_LOCAL = ThreadLocal.withInitial(TextBuilder::new);

    public static TextBuilder threadLocal() {
        TextBuilder result = THREAD_LOCAL.get();
        result.clear();

        return result;
    }
}
