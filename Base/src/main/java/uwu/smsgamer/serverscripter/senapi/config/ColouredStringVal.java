package uwu.smsgamer.serverscripter.senapi.config;

import uwu.smsgamer.serverscripter.senapi.utils.StringUtils;

public class ColouredStringVal extends ConfVal<String> {
    public ColouredStringVal(String name, String defaultVal) {
        super(name, defaultVal);
    }

    public ColouredStringVal(String name, String config, String defaultVal) {
        super(name, config, defaultVal);
    }

    @Override
    public String getValue() {
        return StringUtils.colorize(super.getValue());
    }
}
