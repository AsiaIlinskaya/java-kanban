import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EpicTest {

    // проверьте, что наследники класса Task равны друг другу, если равен их id;
    @Test
    void epicEqualsSameId() {
        Epic epic1 = new Epic("name1", "desc1");
        Epic epic2 = new Epic("name2", "desc2");
        epic1.setId(3);
        epic2.setId(3);
        assertEquals(epic1, epic2);
    }

}