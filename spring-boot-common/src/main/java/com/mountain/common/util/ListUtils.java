package com.mountain.common.util;

import java.util.*;

/**
 * list操作类
 */
public class ListUtils {
    /**
     * @param list     切割集合
     * @param pageSize 分页长度
     * @return List<List<T>> 返回分页数据
     */
    public static <T> List<List<T>> splitList(List<T> list, int pageSize) {
        int listSize = list.size();
        int page = (listSize + (pageSize - 1)) / pageSize;
        List<List<T>> listArray = new ArrayList<List<T>>();
        for (int i = 0; i < page; i++) {
            List<T> subList = new ArrayList<T>();
            for (int j = 0; j < listSize; j++) {
                int pageIndex = ((j + 1) + (pageSize - 1)) / pageSize;
                if (pageIndex == (i + 1)) {
                    subList.add(list.get(j));
                }
                if ((j + 1) == ((j + 1) * pageSize)) {
                    break;
                }
            }
            listArray.add(subList);
        }
        return listArray;
    }

    /**
     * 针对List排序，排序会修改原List
     * List<Integer> list = Arrays.asList(1, 4, 2, 6, 2, 8);
     * list.sort(Comparator.naturalOrder()); 升序排序
     * list.sort(Comparator.reverseOrder());降序排序
     * public static class Person {private String name;private Integer age;public Person(String name, Integer age) {this.name = name;this.age = age;}public Integer getAge() {return age;}
     * personList.add(new Person("a", 2));personList.add(new Person("b", 4));personList.add(new Person("c", 7));
     * List<Person> personList = new ArrayList<>();
     * personList.sort(Comparator.comparingInt(Person::getAge));升序
     * personList.sort(Comparator.comparingInt(Person::getAge).reversed());降序
     *
     * @param <T>  元素类型
     * @param list 被排序的List
     * @param c    {@link Comparator}
     * @return 原list
     * @see Collections#sort(List, Comparator)
     */
    public static <T> List<T> sort(List<T> list, Comparator<? super T> c) {
        list.sort(c);
        return list;
    }

}
