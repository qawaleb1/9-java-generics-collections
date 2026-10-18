package com.example.task02;

import java.io.*;
import java.util.AbstractList;
import java.util.ArrayList;
import java.util.List;

public class SavedList<E extends Serializable> extends AbstractList<E> {

    private final File file;
    private ArrayList<E> list;

    public SavedList(File file) {
        this.file = file;
        if (file.exists() && file.length() > 0) {
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
                @SuppressWarnings("unchecked")
                List<E> loaded = (List<E>) ois.readObject();
                this.list = new ArrayList<>(loaded);
            } catch (Exception e) {
                this.list = new ArrayList<>();
            }
        } else {
            this.list = new ArrayList<>();
        }
    }

    private void save() {
        if (file.getParentFile() != null) {
            file.getParentFile().mkdirs();
        }
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file))) {
            oos.writeObject(list);
        } catch (IOException e) {
            throw new RuntimeException("Ошибка при сохранении списка в файл: " + file.getPath(), e);
        }
    }

    @Override
    public E get(int index) {
        return list.get(index);
    }

    @Override
    public E set(int index, E element) {
        E old = list.set(index, element);
        save();
        return old;
    }

    @Override
    public int size() {
        return list.size();
    }

    @Override
    public void add(int index, E element) {
        list.add(index, element);
        modCount++;
        save();
    }

    @Override
    public E remove(int index) {
        E removed = list.remove(index);
        modCount++;
        save();
        return removed;
    }

    @Override
    public void clear() {
        list.clear();
        modCount++;
        save();
    }
}
