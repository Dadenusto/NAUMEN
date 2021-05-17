package com.company;

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
        int[][] start_goal = new int[2][2];
        // цикл для построения new_map и поиска координат начала и конца
        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < map[i].length; j++) {
                if (map[i][j] == '@') {
                    // если @, то в new_map на этой координате значение 0, а первая строка в start_goal = ее координатам
                    new_map[i][j] = 0;
                    start_goal[0][0] = i;
                    start_goal[0][1] = j;
                } else if (map[i][j] == '.')
                    // если ., то в new_map на этой координате значение -1
                    new_map[i][j] = -1;
                else if (map[i][j] == '#')
                    // если #, то в new_map на этой координате значение -2
                    new_map[i][j] = -2;
                else if (map[i][j] == 'X') {
                    // если Х, то в new_map на этой координате значение -3, а вторая строка в start_goal = ее координатам
                    new_map[i][j] = -3;
                    start_goal[1][0] = i;
                    start_goal[1][1] = j;
                }
            }
        }
        // массив для координаты, из которой придем в X
        int way[]= {-1, -1};
        // переменная для проверки, построились ли соседние клетки
        boolean stop;
        // параметр для построения
        int d = 0;
        do {
            // считаем, что не в сосдних координатах не построилось
            stop = true;
            // проходим все точки
            for (int i = 0; i < map.length && way[0]==-1 && way[1]==-1; i++) {
                for (int j = 0; j < map[i].length && way[0]==-1 && way[1]==-1; j++){
                    // если значение в точке равно проверяемому значению, то ставим значения для соседних с ней
                    if (new_map[i][j] == d)
                        stop = Create_waves(map, new_map, way, stop, d, i, j);
                }
            }
            d++;
        } while (!stop && way[0]==-1);
        /*for(int[] m: new_map){
            for(int s: m){
                System.out.print(s+" ");
            }
            System.out.println();
        }*/
        if (way[0] != - 1 && way[1] != -1) {
            while (true) {
                // если существует точка выше исходной
                if (way[0] - 1 >= 0){
                    // если ее значение на 1 меньше
                    if (new_map[way[0]][way[1]] == new_map[way[0]-1][way[1]] + 1) {
                        // точка в map c данными координатами  становится '+'
                        map[way[0]][way[1]] = '+';
                        //если точка выше равна @, то останавливаемся
                        if (map[way[0]-1][way[1]] == '@')
                            break;
                        // переходим в точку выше
                        way[0]--;
                    }
                }
                // если существует точка ниже исходной
                if (way[0] + 1 <= map.length - 1){
                    if (new_map[way[0]][way[1]] == new_map[way[0]+1][way[1]] + 1) {
                        map[way[0]][way[1]] = '+';
                        if (map[way[0]+1][way[1]] == '@')
                            break;
                        way[0]++;
                    }
                }
                // если существует точка левее исходной
                if (way[1] - 1 >= 0) {
                    if (new_map[way[0]][way[1]] == new_map[way[0]][way[1]-1] + 1) {
                        map[way[0]][way[1]] = '+';
                        if (map[way[0]][way[1]-1] == '@')
                            break;
                        way[1]--;
                    }
                }
                // если существует точка правее исходной
                if (way[1] + 1 <= map[0].length - 1) {
                    if (new_map[way[0]][way[1]] == new_map[way[0]][way[1]+1] + 1) {
                        map[way[0]][way[1]] = '+';
                        if (map[way[0]][way[1]+1] == '@')
                            break;
                        way[1]++;
                    }
                }
            }
            // возвращаем map с построенным маршрутом
            return map;
            }
        else
            // если маршрут не найден возвращаем null
            return null;
        }

    static boolean Create_waves(char[][] map, int[][] new_map, int[] way, boolean stop, int d, int i, int j) {
            // если существует точка выше данной
            if (i - 1 >= 0 && way[1]==-1) {
                // если она не имеет значение, записываем в нее значение на 1 больше
                if (new_map[i - 1][j] == -1) {
                    new_map[i - 1][j] = d + 1;
                    stop = false;
                }
                // если эта точка - конец маршрута, то запаминаем точку, из которой пришли
                else if (new_map[i - 1][j] == -3){
                    way[0]=i;
                    way[1]=j;
                }
            }
            // если существует точка ниже данной и не найден путь до точки
            if (i + 1 <= map.length - 1 && way[1]==-1) {
                //stop = Create_wave(new_map, way, stop, d, i+1, j);
                if (new_map[i + 1][j] == -1) {
                    new_map[i + 1][j] =  d + 1;
                    stop = false;
                }
                else if (new_map[i + 1][j] == -3){
                    way[0]=i;
                    way[1]=j;
                }
            }
            // если существует точка левее данной и не найден путь до точки
            if (j - 1 >= 0 && way[1]==-1) {
                if (new_map[i][j - 1] == -1) {
                    new_map[i][j - 1] = d + 1;
                    stop = false;
                }
                else if (new_map[i][j - 1] == -3){
                    way[0]=i;
                    way[1]=j;
                }
            }
            // если существует точка правее данной и не найден путь до точки
            if (j + 1 <= map[i].length - 1 && way[1]==-1) {
                if (new_map[i][j + 1] == -1) {
                    new_map[i][j + 1] = d + 1;
                    stop = false;
                }
                else if (new_map[i][j + 1] == -3){
                    way[0]=i;
                    way[1]=j;
                }
            }
        return stop;
    }
}
