import java.util.UUID;

import org.junit.jupiter.api.Test;

class MyTest {

    @Test
    void test() {
        System.out.println(UUID.fromString("690A4EC9FBE9235AC8F17661AAE2A713".replaceFirst("([0-9a-fA-F]{8})([0-9a-fA-F]{4})([0-9a-fA-F]{4})([0-9a-fA-F]{4})([0-9a-fA-F]+)", "$1-$2-$3-$4-$5")));
    }
}
