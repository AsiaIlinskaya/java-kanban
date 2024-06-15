package Service;

import Model.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {

    private Node<Task> firstNode;
    private Node<Task> lastNode;
    private int size;
    private final HashMap<Integer, Node<Task>> indexes;

    public InMemoryHistoryManager() {
        size = 0;
        indexes = new HashMap<>();
    }

    @Override
    public List<Task> getHistory() {
        return getTasks();
    }

    @Override
    public void add(Task task) {
        Integer id = task.getId();
        if (indexes.containsKey(id)) {
            Node<Task> node = indexes.get(id);
            removeNode(node);
        }
        linkLast(task);
        indexes.put(id, lastNode);
    }

    @Override
    public void remove(int id) {
        if (indexes.containsKey(id)) {
            Node<Task> node = indexes.get(id);
            removeNode(node);
            indexes.remove(id);
        }
    }

    @Override
    public void clear() {
        indexes.clear();
        size = 0;
        firstNode = null;
        lastNode = null;
    }

    private void linkLast(Task task) {
        if (size == 0) {
            Node<Task> node = new Node<>(null, null, task);
            firstNode = node;
            lastNode = node;
        } else {
            Node<Task> node = new Node<>(lastNode, null, task);
            lastNode.setNext(node);
            lastNode = node;
        }
        size++;
    }

    private List<Task> getTasks() {
        List<Task> tasks = new ArrayList<>(size);
        Node<Task> currentNode = firstNode;
        while (currentNode != null) {
            tasks.add(currentNode.getValue());
            currentNode = currentNode.getNext();
        }
        return tasks;
    }

    private void removeNode (Node<Task> node) {
        Node<Task> nextNode = node.getNext();
        Node<Task> prevNode = node.getPrev();
        if (nextNode != null) {
            nextNode.setPrev(prevNode);
        } else {
            lastNode = prevNode;
        }
        if (prevNode != null) {
            prevNode.setNext(nextNode);
        } else {
            firstNode = nextNode;
        }
        node.setPrev(null);
        node.setNext(null);
        size--;
    }

}
