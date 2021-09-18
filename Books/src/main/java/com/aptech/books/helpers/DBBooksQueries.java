package com.aptech.books.helpers;

import com.aptech.books.models.Author;
import com.aptech.books.models.Book;
import com.aptech.books.models.Category;

public class DBBooksQueries {

    public static String getBook() {
        return "select * from book ORDER BY bookID ASC";
    }

    public static String insertBook(Book b) {
        return String.format("INSERT INTO book(title, content, price, author, releaseDate, image, category) VALUES ('%s','%s', %d, '%s', '%s', '%s', '%s')"
                    , b.getTitle()
                    , b.getContent()
                    , b.getPrice()
                    , b.getAuthor()
                    , b.getReleaseDate()
                    , b.getImage()
                    , b.getCategory());
    }
    
    public static String insertAuthor(Author a) {
        return String.format("INSERT INTO author(name) VALUES ('%s')", a.getName());
    }
    
    public static String insertCategory(Category c) {
        return String.format("INSERT INTO category(name) VALUES ('%s')", c.getName());
    }

    public static String getOneAuthor(Author authorName) {
        return String.format("SELECT name FROM author WHERE name = '%s'", authorName.getName());
    }
    
    public static String getOneCategory(Category categoryName) {
    	return String.format("SELECT name FROM category WHERE name = '%s'", categoryName.getName());
    }
    
    public static String getAuthor() {
    	return "select distinct name from author order by authorID DESC";
    }
    
    public static String getCategory() {
        return "select distinct name from category order by categoryID DESC";
    }
    
    public static String updateBook(Book b) {
        return String.format("UPDATE book SET title='%s', content='%s', price=%d, author='%s', releaseDate='%s', image='%s', category='%s' WHERE bookID =%d"
                , b.getTitle()
                , b.getContent()
                , b.getPrice()
                , b.getAuthor()
                , b.getReleaseDate()
                , b.getImage()
                , b.getCategory()
                , b.getBookID());
    }

    public static String getWebUserByUsernameAndPassword(String username, String password) {
        return String.format("select * from webuser where uid='%s' and password='%s'", username, password);
    }
    
    public static String loadBook(String bookID) {
    	return "select * from book where bookID=" + bookID;
    }
    
    public static String deleteBook(String bookID) {
    	return "delete from book where bookID=" + bookID;
    }

}