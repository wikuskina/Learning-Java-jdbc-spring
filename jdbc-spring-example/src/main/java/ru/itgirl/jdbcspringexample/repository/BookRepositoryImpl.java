package ru.itgirl.jdbcspringexample.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import ru.itgirl.jdbcspringexample.Books;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Repository
public class BookRepositoryImpl implements BookRepository {
    @Autowired
    private DataSource dataSource;
    public BookRepositoryImpl(DataSource dataSource) {
        this.dataSource = dataSource;
    }
    @Override
    public List <Books> findAllBooks () {
        //создаем пустой список, в который сложим наши книги из БД
        List <Books> results = new ArrayList<>();

        //наш SQL-запрос
        String SQL_findAllBooks = "Select * from books";

        //Создаём новые объекты Connection и Statement
        //Использование try-with-resources необходимо для гарантированного закрытия connection и statement,
        //вне зависимости от успешности операции.

        //Создаем подключение к БД
        try (Connection connection= dataSource.getConnection();
        Statement statement = connection.createStatement();
        //если подключение к БД успешно выполненно, то передаем наш SQL запрос
        ResultSet resultSet = statement.executeQuery(SQL_findAllBooks)){
            //ResultSet - итерируемый объект.
            //Пока есть что доставать, идём по нему и преобразовываем sql-строки в объекты класса Book.
            //Добавляем полученный объект в ArrayList.
            // next() moves to the next row SQL
            while (resultSet.next()) {
                Books books = convertRowToBook(resultSet);
                results.add(books);
            }
        }
        catch (SQLException e) {
            //Если операция провалилась, обернём пойманное исключение в непроверяемое и пробросим дальше(best-practise)
            throw new IllegalStateException(e);
        }
        //Возвращаем полученный в результате операции ArrayList
        return results;
    }

    @Override
    public Books getBookId(Long id) {
        //наш SQL-запрос
        String SQL_findID= "Select * from books where id=?;";
        ResultSet resultSet = null;
        try (Connection connection= dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(SQL_findID)) {
            statement.setLong(1, id);
            resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return convertRowToBook(resultSet);
            }
            else {
                return null;
            }
        }
        catch (SQLException e) {
            //Если операция провалилась, обернём пойманное исключение в непроверяемое и пробросим дальше(best-practise)
            throw new IllegalStateException(e);
        }
        finally {
            try {
                if (resultSet != null) {
                    resultSet.close();
                }
            } catch (SQLException e) {
                throw new IllegalStateException(e);
            }
        }

    }
    private Books convertRowToBook(ResultSet resultSet) throws SQLException {
        Long id = resultSet.getLong("id");
        String name = resultSet.getString("title");
        return new Books(id, name);
    }
}
