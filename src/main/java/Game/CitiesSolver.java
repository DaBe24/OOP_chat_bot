package Game;

import java.util.*;
import java.io.*;


public class CitiesSolver
{
    private static Map<Character, Set<String>> index;
    private Set<String> usedCities = new HashSet<String>();
    private String last;


    public CitiesSolver()  throws IOException {
        final Set<String> cities = new HashSet<String>();

        // Загрузим города из файла
        final BufferedReader in = new BufferedReader(new FileReader("src/main/resources/cities.txt"));
        for (String city; (city = in.readLine()) != null;) {
            cities.add(city.toUpperCase()); // и сложим все имена в этот массив
        }
        in.close();
        index = createIndex(cities);
    }

    public String getUsedCitys(){
        StringJoiner joiner = new StringJoiner(", ");
        for (String city : usedCities){
            joiner.add(city);
        }
        return joiner.toString();
    }


    /**
     * Проверка слова что это город
     *
     * @param city - город для выбора первой буквы
     * @return является ли городом
     */
    public Boolean isCity(String city)
    {
        Set<String> citiesAtThisLater = index.get(getFirstChar(city.toUpperCase()));
        return citiesAtThisLater != null && citiesAtThisLater.contains(city.toUpperCase());
    }


    /**
     * Вернуть последнеий введёный город
     *
     * @return последний введёный город
     */
    public String getLast(){
        return last;
    }


    /**
     * Проверка что город ещё не называли
     *
     * @param city - город для выбора первой буквы
     * @return является ли городом
     */
    public Boolean isUsedCity(String city)
    {
        if (!usedCities.contains(city.toUpperCase())){
            usedCities.add(city.toUpperCase());
            last = city.toUpperCase();
            return false;
        }
        return true;
    }


    /**
     * Проверка первой и последней буквы
     *
     * @param city - город для выбора первой буквы
     * @return первая и последняя буква совподает
     */
    public boolean checkRuls(String city){
        return getLastChar(last).equals(getFirstChar(city));
    }


    /**
     * Получить следующий город
     *
     * @param city - город для выбора первой буквы
     * @return Следующий город
     */
    public String getNextCity(String city)
    {
        for (String nextCity : index.get(getLastChar(city))){
            if (!isUsedCity(nextCity)) {
                usedCities.add(nextCity);
                last = nextCity;
                return nextCity;
            }
        }
        return "";
    }


    /**
     * Получение первой буквы для сравнения
     *
     * @param city - город для выбора первой буквы
     * @return первая буква
     */
    private static Character getFirstChar(String city)
    {
        return city.charAt(0);
    }


    /**
     * Получение последней буквы для сравнения, с пропуском "неправильных" букв
     *
     * @param city - город для выбора последней буквы
     * @return последняя буква
     */
    private static Character getLastChar(String city)
    {
        int pos = city.length() - 1;
        char lastChar = city.toUpperCase().charAt(pos);
        if (city.toUpperCase().charAt(pos) == 'Ё') {
            return 'Е';
        }
        else if (lastChar == 'Ь' || lastChar == 'Ы' || lastChar == 'Ъ') {
            pos--;
        }
        return city.toUpperCase().charAt(pos);
    }


    /**
     * Инициализация коллекции городов для поиска
     *
     * @param cities - города для инициализации
     * @return коллекция городов
     *
     * key - первая буква города, value - имена городов начинающихся на эту букву
     */
    private static Map<Character, Set<String>> createIndex(Set<String> cities)
    {
        Map<Character, Set<String>> index = new HashMap<Character, Set<String>>();
        for (String city : cities) {
            Set<String> cs = index.get(getFirstChar(city));
            if (cs == null) index.put(getFirstChar(city), cs = new HashSet<String>());
            cs.add(city);
        }
        return index;
    }
}