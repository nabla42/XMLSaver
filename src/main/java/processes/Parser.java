package processes;


import java.io.InputStream;
import java.io.Serializable;
import java.util.List;

public interface Parser {
    /**
     * Метод для обработки
     */
    void process(InputStream is);

    /**
     * Получение результата.
     *
     * @return      массив сериализуемых объектов
     */
    <T extends Serializable> List<T> getResult();
}
