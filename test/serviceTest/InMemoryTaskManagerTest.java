package serviceTest;

import org.junit.jupiter.api.Test;
import service.InMemoryTaskManager;
import service.Managers;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

class InMemoryTaskManagerTest extends TaskManagerTest<InMemoryTaskManager> {

    @BeforeEach
    void prepareTaskManager() {
        setManager(new InMemoryTaskManager());
    }

    @AfterEach
    void cleanHistory() {
        Managers.getDefaultHistory().clear();
    }

    @Test
    @Override
    protected void getAllTasksTest() {
        super.getAllTasksTest();
    }

    @Test
    @Override
    protected void getAllEpicsTest() {
        super.getAllEpicsTest();
    }

    @Test
    @Override
    protected void getAllSubtasksTest() {
        super.getAllSubtasksTest();
    }

    @Test
    @Override
    protected void removeAllTasksTest() {
        super.removeAllTasksTest();
    }

    @Test
    @Override
    protected void removeAllEpicsTest() {
        super.removeAllEpicsTest();
    }

    @Test
    @Override
    protected void removeAllSubtasksTest() {
        super.removeAllSubtasksTest();
    }

    @Test
    @Override
    protected void putAndGetTaskTest() {
        super.putAndGetTaskTest();
    }

    @Test
    @Override
    protected void putAndGetEpicTest() {
        super.putAndGetEpicTest();
    }

    @Test
    @Override
    protected void putAndGetSubtaskTest() {
        super.putAndGetSubtaskTest();
    }

    @Test
    @Override
    protected void removeTaskTest() {
        super.removeTaskTest();
    }

    @Test
    @Override
    protected void removeEpicTest() {
        super.removeEpicTest();
    }

    @Test
    @Override
    protected void removeSubtaskTest() {
        super.removeSubtaskTest();
    }

    @Test
    @Override
    protected void updateTaskTest() {
        super.updateTaskTest();
    }

    @Test
    @Override
    protected void updateEpicTest() {
        super.updateEpicTest();
    }

    @Test
    @Override
    protected void updateSubtaskTest() {
        super.updateSubtaskTest();
    }

    @Test
    @Override
    protected void getSubtasksTest() {
        super.getSubtasksTest();
    }

    @Test
    @Override
    protected void getHistoryTest() {
        super.getHistoryTest();
    }

    @Test
    @Override
    protected void taskHistoryMutabilityTest() {
        super.taskHistoryMutabilityTest();
    }

    @Test
    @Override
    protected void taskMutabilityTest() {
        super.taskMutabilityTest();
    }

    @Test
    @Override
    protected void taskTimeIntersectionTest() {
        super.taskTimeIntersectionTest();
    }

}