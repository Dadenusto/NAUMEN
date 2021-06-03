package com.company;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * Интерфейс поиска маршрута
 */
public interface RouteFinder {
    /**
     * Поиск кратчайшего маршрута между двумя точками
     * Поиск будет проводится при помощи алгоритма Ли
     * Создается массив new_map типа int размерности как у map
     * В матрицу 2х2 start_goal типа int запишем координаты '@' и 'X' соотвествеено
     * Записываем данные в new_map по следующему правилу:
     * где в map '@' в new_map 0
     * где в map '.' в new_map -1
     * где в map '#' в new_map -2
     * где в map 'X' в new_map -3
     * Массив way типа int размерности 2 будет хранить координаты точки, из которой попадаем в 'X'
     * В цикле do while производим построение лаберинта по алгоритму Ли:
     * 0)задаем начальный параметр d = 0
     * 1)задаем параметр stop=false
     * 2)ищем элемент, равный d
     * 3.1)если не найден, прекращаем цикл
     * 3.2)если найден, то соседним точкам по горизонтале и вертикали ставим значение d+1 и stop=false
     * 3.3)если одна из соседних точек является -3, т.е. 'X', то way ставим значения точки,
     * 4)проверяем на критерий остонва: если way != {-1, -1} и stop = false, то d++, возвращаемся к 1) (если stop=true, то значит, что было невозможно построить соседние точки для данного d), иначе 5
     * 5.1)если way == {-1, -1}, то возвращаем null
     * 5.2)иначе путь найден
     * 6)для точки с координатами way ищем среди соседних ту, значение которой на 1 меньше, присваеваем в map точке с координатами way '+'
     * 7.1)если соседняя точка является '@', то оставливаем цикл и возвращаем матрицу map с построенным путем
     * 7.2)иначе переходим в точку со значением на 1 меньше и возвращаемся к шагу 6
     * @return map
     */
    static char[][] findRoute(char[][] map) {
        // new_map для поиска пути
        int[][] new_map = new int[map.length][map[0].length];
        // матрица 2x2 для координат @ и Х
        int[] start_goal = new int[4];
        int line_count = map.length;
        int row_count =  map[0].length;
        // цикл для построения new_map и поиска координат начала и конца
        IntStream.range(0, line_count)
                .forEach(i -> {
                    IntStream.range(0, row_count)
                            .forEach(j-> {
                                switch (map[i][j]) {
                                    case '.':
                                        // если ., то в new_map на этой координате значение -1
                                        new_map[i][j] = -1;
                                        break;
                                    case '#':
                                        // если #, то в new_map на этой координате значение -2
                                        new_map[i][j] = -2;
                                        break;
                                    case 'X':
                                        // если Х, то в new_map на этой координате значение -3, а в start_goal элементах 3 и 4 = ее координатам
                                        new_map[i][j] = -3;
                                        start_goal[2] = i;
                                        start_goal[3] = j;
                                        break;
                                    case '@':
                                        // если @, то в new_map на этой координате значение 0, а start_goal элементах 1 и 2 = ее координатам
                                        new_map[i][j] = 0;
                                        start_goal[0] = i;
                                        start_goal[1] = j;
                                        break;
                                }
                    });
        });
        // координаты, из которой придем в X
        int way1=-1;
        int way2=-1;
        // переменная для проверки, построились ли соседние клетки
        // параметр для построения
        int d = 0;
        // координаты точки 0
        int[]arr =  new int[]{start_goal[0], start_goal[1]};
        int arr_length=arr.length;
        boolean stop = false;
        do{
            // массив для точек d+1
            int []new_arr = new int[(d+1)*4*2];
            int g = 0;
            stop = false;
            for (int k = 0; k<arr_length;k+=2) {
                // берем точку, в которых расположенно d
                int i = arr[k];
                int j = arr[k+1];
                if (new_map[i][j] == d) {
                    if (i - 1 >= 0 && way2 == -1) {
                        // если она не имеет значение, записываем в нее значение на 1 больше
                        switch (new_map[i - 1][j]) {
                            case -1 -> {
                                new_map[i - 1][j] = d + 1;
                                new_arr[g]= i - 1;
                                new_arr[g+1]= j;
                                g+=2;
                                stop = true;
                            }
                            case -3 -> {
                                way1 = i;
                                way2 = j;
                                break;
                            }
                        }
                    }
                    // если существует точка ниже данной и не найден путь до точки
                    if (i + 1 <= line_count - 1 && way2 == -1) {
                        switch (new_map[i + 1][j]) {
                            case -1 -> {
                                new_map[i + 1][j] = d + 1;
                                new_arr[g]= i + 1;
                                new_arr[g+1]= j;
                                g+=2;
                                stop = true;
                            }
                            case -3 -> {
                                way1 = i;
                                way2 = j;
                                break;
                            }
                        }
                    }
                    // если существует точка левее данной и не найден путь до точки
                    if (j - 1 >= 0 && way2 == -1) {
                        switch (new_map[i][j - 1]) {
                            case -1 -> {
                                new_map[i][j - 1] = d + 1;
                                new_arr[g]= i;
                                new_arr[g+1]= j-1;
                                g+=2;
                                stop = true;
                            }
                            case -3 -> {
                                way1 = i;
                                way2 = j;
                                break;
                            }
                        }
                    }
                    // если существует точка правее данной и не найден путь до точки
                    if (j + 1 <= row_count - 1 && way2 == -1) {
                        switch (new_map[i][j + 1]) {
                            case -1 -> {
                                new_map[i][j + 1] = d + 1;
                                new_arr[g]= i;
                                new_arr[g+1]= j + 1;
                                g+=2;
                                stop = true;
                            }
                            case -3 -> {
                                way1 = i;
                                way2 = j;
                                break;
                            }
                        }
                    }
                }
            }
            arr = new_arr;
            arr_length=arr.length;
            d++;
        } while (stop && way1 == -1);
        if (way1 != - 1) {
            int nm = new_map[way1][way2];
            while (true) {
                // если существует точка выше исходной
                if (way1 - 1 >= 0) {
                    // если ее значение на 1 меньше
                    if (nm == new_map[way1 - 1][way2] + 1) {
                        // точка в map c данными координатами  становится '+'
                        map[way1][way2] = '+';
                        //если точка выше равна @, то останавливаемся
                        if (map[way1 - 1][way2] == '@')
                            break;
                        // переходим в точку выше
                        way1--;
                        nm = new_map[way1][way2];
                    }
                }
                // если существует точка ниже исходной
                if (way1 + 1 <= map.length - 1) {
                    if (nm == new_map[way1 + 1][way2] + 1) {
                        map[way1][way2] = '+';
                        if (map[way1 + 1][way2] == '@')
                            break;
                        way1++;
                        nm = new_map[way1][way2];
                    }
                }
                // если существует точка левее исходной
                if (way2 - 1 >= 0) {
                    if (nm == new_map[way1][way2 - 1] + 1) {
                        map[way1][way2] = '+';
                        if (map[way1][way2 - 1] == '@')
                            break;
                        way2--;
                        nm = new_map[way1][way2];
                    }
                }
                // если существует точка правее исходной
                if (way2 + 1 <= map[0].length - 1) {
                    if (nm == new_map[way1][way2 + 1] + 1) {
                        map[way1][way2] = '+';
                        if (map[way1][way2 + 1] == '@')
                            break;
                        way2++;
                        nm = new_map[way1][way2];
                    }
                }
            }
            // возвращаем map с построенным маршрутом
            return map;
        }
        else {
            return new char[][]{{'n', 'u', 'l', 'l'}};
        }
    }
}
