package javaapplication45;

import java.sql.*;
import java.util.Scanner;
import java.util.InputMismatchException;

public class javaapplication45 {

    private static final String url = "jdbc:mysql://localhost:3306/BookDealer2?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC";
    private static final String user = "root";
    private static final String password = ""; /*add pasword here betwen ""*/
    private static Scanner input = new Scanner(System.in);
    private static Statement st = null;
    private static ResultSet rs = null;
    private static PreparedStatement pst = null;

    public static void main(String[] args) {
        try (Connection connection = DriverManager.getConnection(url, user, password)) {

            System.out.println("Connection succeeded!");

            //st = connection.createStatement();
            st = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            menu();
        } catch (Exception ex) {
            ex.printStackTrace();
            System.out.println("Something went wrong, please try again!");
        }
    }

    private static void menu() throws SQLException {
        boolean exit = false;
        while (!exit) {
            try {
                System.out.println("");
                System.out.println("******************** Main menu ********************");
                System.out.println("1. Author");
                System.out.println("2. Book");
                System.out.println("3. Customer");
                System.out.println("4. Borrow a book");
                System.out.println("5. Return a book");
                System.out.println("6. Remove option");
                System.out.println("0. Exit");

                int selection = input.nextInt();
                input.nextLine();

                switch (selection) {
                    case 1:
                        author();
                        break;
                    case 2:
                        book();
                        break;
                    case 3:
                        customer();
                        break;
                    case 4:
                        borrowBook();
                        break;
                    case 5:
                        returnBook();
                        break;
                    case 6:
                        remove_option();
                        break;
                    case 0:
                        System.out.println("See you next time!");
                        exit = true;
                }
            } catch (InputMismatchException e) {
                System.out.println("Something went wrong please try again!!!!!!");
                input.next();
                menu();
            }
        }
    }

    private static void author() throws SQLException {

        boolean exit = false;
        while (!exit) {
            System.out.println();
            System.out.println("******************** Author library ********************");
            System.out.println("1. Show all authors");
            System.out.println("2. Show author");
            System.out.println("3. Create author");
            System.out.println("4. Update author");
            System.out.println("0. Go back");

            int selection = input.nextInt();
            input.nextLine();

            switch (selection) {
                case 1:
                    list_table("author");
                    break;
                case 2:
                    list_author();
                    break;
                case 3:
                    create_author();
                    break;
                case 4:
                    update_author();
                    break;
                default:
                    exit = true;
            }
        }
    }

    private static void list_author() throws SQLException {
        list_table("author");

        System.out.println("Choose which author you want to have information about. Enter in author_id: ");
        int author_id = input.nextInt();

        rs = st.executeQuery("SELECT * FROM booksAuthorDetails" + " WHERE AuthorId = " + author_id + ";");

        if (rs.first()) {
            rs.beforeFirst();
            while (rs.next()) {

                if (rs.isFirst()) {
                    System.out.println("Author Id: " + rs.getString("AuthorId") + "\n");
                    System.out.println("Author Name: " + rs.getString("AuthorName") + "\n");
                    System.out.println("Release Year: " + rs.getString("ReleaseYear") + "\n");
                    System.out.println("Book Grade: " + rs.getString("BookGrade") + "\n");
                    System.out.println("Books: ");
                }
                System.out.println("Isbn: " + rs.getString("Isbn") + "\n" + "Book Title: " + rs.getString("BookTitle") + "\n\n");
            }
        } else {
            System.out.println("The author id you have entered, doesn't have any books registered!");
        }
    }

    private static void create_author() throws SQLException {
        System.out.println("Enter fullname of new author");
        String newAuthor = input.nextLine();
        st.executeUpdate("INSERT INTO author(author_name) VALUE('" + newAuthor + "');");
        System.out.println(newAuthor + " has been created!");
    }

    private static void update_author() throws SQLException {
        list_table("author");

        System.out.println("Which author would you like to change name for? Enter auther id");
        int author_id = input.nextInt();
        input.nextLine();
        rs = st.executeQuery("SELECT * FROM author" + " WHERE author_id = " + author_id + ";");
        if (rs.first()) {
            rs.beforeFirst();
            System.out.println("Enter the new author name");
            String author_name = input.nextLine();
            pst = st.getConnection().prepareStatement("UPDATE Author SET author_name = ? WHERE author_id = ?");

            pst.setString(1, author_name);
            pst.setInt(2, author_id);

            pst.executeUpdate();
            System.out.println("Changes has been saved!");
        } else {
            System.out.println("The author id you have chosen, isn't registered!");
        }
    }

    private static void book() throws SQLException {
        boolean exit = false;
        while (!exit) {
            System.out.println();
            System.out.println("******************** Book library ********************");
            System.out.println("1. Show all books");
            System.out.println("2. Show book");
            System.out.println("3. Create book and connect to author");
            System.out.println("4. Update book");
            System.out.println("0. Go back");

            int selection = input.nextInt();
            input.nextLine();

            switch (selection) {
                case 1:
                    list_table("bookList");
                    break;
                case 2:
                    list_book();
                    break;
                case 3:
                    create_book();
                    break;
                case 4:
                    update_book();
                    break;
                default:
                    exit = true;
            }
        }
    }

    private static void list_book() throws SQLException {
        list_table("bookList");

        System.out.println("Choose which book you want to have information about. Enter in isbn: ");
        int isbn = input.nextInt();
        space();

        rs = st.executeQuery("SELECT * FROM booksAuthorDetails" + " WHERE Isbn = " + isbn + ";");

        if (rs.first()) {
            rs.beforeFirst();
            while (rs.next()) {

                if (rs.isFirst()) {
                    System.out.println("Isbn Number: " + rs.getString("Isbn") + "\n");
                    System.out.println("Book Title: " + rs.getString("BookTitle") + "\n");
                    System.out.println("Release Year: " + rs.getString("ReleaseYear") + "\n");
                    System.out.println("Book Grade: " + rs.getString("BookGrade") + "\n");

                    System.out.println("Authors:");
                }
                System.out.println("Author Id: " + rs.getString("AuthorId") + "\n" + "Author Name: " + rs.getString("AuthorName") + "\n\n");
            }
        } else {
            System.out.println("The isbn you have entered, don't have any author registered!");
        }
    }

    private static void create_book() throws SQLException {
        System.out.println("Enter book title for new book");
        String newBookTitle = input.nextLine();
        System.out.println("Enter release year for new book");
        int newReleaseYear = input.nextInt();
        System.out.println("Enter book grade (1-5) for new book");
        int newBookGrade = input.nextInt();
        input.nextLine();
        System.out.println("How many authors do the book have");
        int amountOfAuthor = input.nextInt();
        input.nextLine();

//        System.out.println("Does this book author exist in the following list?");
//        list_table("author");
//        System.out.println("Enter : yes/no");
//        String yesorno = input.nextLine();
//        if (!"yes".equalsIgnoreCase(yesorno)) {
//            create_author();
//            list_table("author");
//        }
//
//        System.out.println("Enter author_id: ");
//        int author_id = input.nextInt();
//        input.nextLine();
        /*nytt*/
        System.out.println("How many of this edition would you like to have in the library?");
        int editions = input.nextInt();
        input.nextLine();
        st.executeUpdate("INSERT INTO book(book_title, release_year, grade_id) VALUE('" + newBookTitle + "','" + newReleaseYear + "','" + newBookGrade + "');");
        space();

        rs = st.executeQuery("SELECT * FROM bookisbn;");
        rs.next();

        System.out.println("Isbn for new book: " + rs.getString("isbn"));
        String isbn = rs.getString("isbn");
        space();

        System.out.println("This book contain " + amountOfAuthor + " authors.");
        for (int i = 1; i <= amountOfAuthor; i++) {
            System.out.println("");
            System.out.println("Does this book author exist in the following list?");
            list_table("author");
            System.out.println("Enter : yes/no");
            String yesorno = input.nextLine();
            if (!"yes".equalsIgnoreCase(yesorno)) {
                create_author();
                list_table("author");
            }
            System.out.println("Enter author_id: ");
            int author_id = input.nextInt();
            input.nextLine();
            st.executeUpdate("INSERT INTO book_author(isbn, author_id)VALUE('" + isbn + "','" + author_id + "');");
        }

        for (int i = 1; i <= editions; i++) {
            st.executeUpdate("INSERT INTO books(isbn) VALUE('" + isbn + "');");
        }
        System.out.println("Book has been created!");

    }

    private static void update_book() throws SQLException {
        list_table("book");

        System.out.println("Which book would you like to change name for? Enter isbn: ");
        int isbn = input.nextInt();
        input.nextLine();
        rs = st.executeQuery("SELECT * FROM book" + " WHERE isbn = " + isbn + ";");
        if (rs.first()) {
            rs.beforeFirst();
            System.out.println("Enter the new book title: ");
            String book_title = input.nextLine();
            pst = st.getConnection().prepareStatement("UPDATE book SET book_title = ? WHERE isbn = ?");

            pst.setString(1, book_title);
            pst.setInt(2, isbn);

            pst.executeUpdate();
            System.out.println("Changes has been saved!");
        } else {
            System.out.println("The isbn you have chosen, isn't registered!");
        }
    }

    private static void customer() throws SQLException {
        boolean exit = false;
        while (!exit) {
            System.out.println();
            System.out.println("******************** Customer ********************");
            System.out.println("1. Show all customers");
            System.out.println("2. Show customer");
            System.out.println("3. Create customer");
            System.out.println("4. Update customer");
            System.out.println("0. Go back");

            int selection = input.nextInt();
            input.nextLine();

            switch (selection) {
                case 1:
                    list_table("customer");
                    break;
                case 2:
                    list_customer();
                    break;
                case 3:
                    create_customer();
                    break;
                case 4:
                    update_customer();
                    break;
                default:
                    exit = true;
            }
        }
    }

    private static void list_customer() throws SQLException {

        list_table("customer");

        System.out.println("Choose which customer you want to have information about. Enter customer id: ");
        int customer_id = input.nextInt();
        space();

        rs = st.executeQuery("SELECT * FROM customerList" + " WHERE CustomerId = " + customer_id + ";");

        if (rs.first()) {
            rs.beforeFirst();
            while (rs.next()) {

                if (rs.isFirst()) {
                    System.out.println("Customer id: " + rs.getString("CustomerId") + "\n");
                    System.out.println("Social Security Number: " + rs.getString("SocialSecurityNumber") + "\n");
                    System.out.println("Customer Name: " + rs.getString("CustomerName") + "\n");

                    System.out.println("Books:");
                }
                System.out.println("Book Title: " + rs.getString("BookTitle") + "\n" + "Book Id: " + rs.getString("BookId") + "\n" + "Borrowed date: " + rs.getString("RentDate") + "\n" + "Expected return date: " + rs.getString("ExpectedReturnDate") + "\n" + "Return Status: " + rs.getString("ReturnStatus") + "\n\n");
            }
        } else {
            System.out.println("The customer id you have chosen, don't have any registerations!");
        }
    }

    private static void create_customer() throws SQLException {
        System.out.println("Enter social security number of new customer: \n'YYYYMMDD-XXXX'");
        String social_security_number = input.nextLine();
        System.out.println("Enter first name of new customer");
        String first_name = input.nextLine();
        System.out.println("Enter last name of new customer");
        String last_name = input.nextLine();
        st.executeUpdate("INSERT INTO customer(social_security_number, first_name, last_name) VALUE('" + social_security_number + "','" + first_name + "','" + last_name + "');");
        System.out.println("Customer has been created!");
    }

    private static void update_customer() throws SQLException {
        list_table("customer");

        System.out.println("Which customer would you like to change name for? Enter customer id");
        int customer_id = input.nextInt();
        input.nextLine();
        rs = st.executeQuery("SELECT * FROM customer" + " WHERE customer_id = " + customer_id + ";");
        if (rs.first()) {
            rs.beforeFirst();
            System.out.println("Enter the new customer first name");
            String first_name = input.nextLine();
            System.out.println("Enter the new customer last name");
            String last_name = input.nextLine();
            pst = st.getConnection().prepareStatement("UPDATE customer SET first_name = ?, last_name = ? WHERE customer_id = ?");

            pst.setString(1, first_name);
            pst.setString(2, last_name);
            pst.setInt(3, customer_id);

            pst.executeUpdate();
            System.out.println("Changes has been saved!");
        } else {
            System.out.println("The customer id you have chosen, isn't registered!");
        }
    }

    private static void borrowBook() throws SQLException {
        try {
            System.out.println("******************** Customer ********************");

            list_table("customer");

            System.out.println("Enter customer id for borrower: ");
            int borrower = input.nextInt();
            input.nextLine();

            System.out.println("******************** Books ********************");

            list_table("bookid");

            System.out.println("Enter book id to borrow");
            int borrow = input.nextInt();
            input.nextLine();

            st.executeUpdate("INSERT INTO rental(customer_id, book_id) VALUE('" + borrower + "','" + borrow + "');");
            space();

            System.out.println("You have now borrowed the book!");
        } catch (Exception E) {

            System.out.println("Something went wrong, please try again!");
        }
    }

    private static void returnBook() throws SQLException {
        list_table("customerListWithUnicInfo");

        System.out.println("Which customer would you like to change return status on? Enter Customer id");
        int customer_id = input.nextInt();
        input.nextLine();

        System.out.println("Which Book would you like to change return status on? Enter Book id");
        int book_id = input.nextInt();
        input.nextLine();
        rs = st.executeQuery("SELECT * FROM customerListWithUnicInfo" + " WHERE CustomerId = " + customer_id + ";");
        rs = st.executeQuery("SELECT * FROM customerListWithUnicInfo" + " WHERE BookId = " + book_id + ";");
        if (rs.first()) {
            rs.beforeFirst();
            System.out.println("Enter todays date: 'YYYY-MM-DD HH:MM:SS'");
            String return_date = input.nextLine();

            pst = st.getConnection().prepareStatement("UPDATE customerListWithUnicInfo SET ReturnStatus = ? WHERE CustomerId = ? AND BookId = ?");

            pst.setString(1, return_date);
            pst.setInt(2, customer_id);
            pst.setInt(3, book_id);

            pst.executeUpdate();
            System.out.println("Changes has been saved!");
            int outCome1_1 = st.executeUpdate("delete from rental where book_id =" + book_id + ";");
            if (outCome1_1 == 1) {
                System.out.println("Due to corona virus, we return the books to manfucture for cleaning for next borrower safty!");
            }
//            rs = st.executeQuery("SELECT * FROM books WHERE book_id =" + book_id + ";");
//                        rs.next();
//                        String isbn = rs.getString("isbn");
//            st.executeUpdate("delete from book_author where isbn=" + isbn + ";");
        } else {
            System.out.println("Something went wrong, check that you have chosen the right customer for the right book!");
        }
    }

    private static void remove_option() throws SQLException {
        boolean exit = false;
        while (!exit) {
            System.out.println("******************** Remove option ********************");
            System.out.println("");
            System.out.println("  ______________________________________________________");
            System.out.println(" /_____________________________________________________/|");
            System.out.println("|                                                     | |");
            System.out.println("|               Remove option                         | |");
            System.out.println("|                                                     | |");
            System.out.println("|  1. Remove edition who isn't borrowed from library  | |");
            System.out.println("|  2. Remove book from library                        | |");
            System.out.println("|  3. Remove author from library                      | |");
            System.out.println("|     if author doesn't have books registered         | |");
            System.out.println("|  4. Remove customer                                 | |");
            System.out.println("|  0. Go back                                         | |");
            System.out.println("|                                                     | |");
            System.out.println("|_____________________________________________________|/");

            int remove = input.nextInt();

            switch (remove) {
                case 1://Ta bort boken på individ nivå, boken får inte vara rentad!

                    String sql2 = "SELECT * FROM bookInfoNotBorrowed;";
                    printTable(sql2);
                    System.out.println("Enter book_id for the book you want to remove: ");
                    int book_id = input.nextInt();
                    input.nextLine();
                    try {
                        int outCome1_1 = st.executeUpdate("delete from books where book_id = " + book_id + ";");
                        if (outCome1_1 == 1) {
                            System.out.println("The book with ID: " + book_id + " is now removed!\n");
                        } else {
                            System.out.println("The book id you have chosen, haven't been returned yet to be removed from here!");
                        }
                    } catch (Exception E) {
                        System.out.println("The book id you have chosen, haven't been returned yet to be removed from here!");
                    }
                    break;
                case 2://Ta bort isbn helt och hållet från book_author & book table.
                    String sql3 = "SELECT * FROM book;";
                    printTable(sql3);
                    System.out.println("Enter the isbn for the book you want to remove: ");
                    int isbn = input.nextInt();
                    input.nextLine();
                    try {
//                        int outCome1_1 = st.executeUpdate("delete from book_author where isbn=" + isbn + ";");
//                        if (outCome1_1 == 1) {
                            st.executeUpdate("delete from book_author where isbn=" + isbn + ";");
                            st.executeUpdate("delete from book where isbn=" + isbn + ";");
                            System.out.println("The book with isbn: " + isbn + " is now removed!\n");
//                        } else {
//                            System.out.println("The book isbn you have chosen, does have a edition borrowed!");
//                        }
                    } catch (Exception E) {
                        System.out.println("The book isbn you have chosen, does have a edition borrowed!");
                    }
                    break;
                case 3://Tar bort auhtor från author table om authorn inte har registrerade böcker på sig.
                    String sql1 = "SELECT * FROM author;";
                    printTable(sql1);
                    System.out.println("Enter author id for the author you want to remove: ");
                    int author_id1 = input.nextInt();
                    input.nextLine();
                    try {
                        int outCome1_1 = st.executeUpdate("DELETE FROM author WHERE author_id =" + author_id1 + ";");
                        if (outCome1_1 == 1) {
                            System.out.println("The author with ID: " + author_id1 + " is now removed!\n");
                        } else {
                            System.out.println("The author id you have chosen, do have books registered!");
                        }
                    } catch (Exception e) {
                        System.out.println("The author id you have chosen, do have books registered!");
                    }
                    break;
                case 4://Tar bort customer, går ej att ta bort customer förren boken återlämnad.

                    String sql6 = "SELECT * FROM customer;";
                    printTable(sql6);
                    System.out.println("Enter customer id for the customer you want to remove: ");
                    int customer_id = input.nextInt();
                    input.nextLine();
                    try {
                        int outCome1_1 = st.executeUpdate("DELETE FROM customer WHERE customer_id =" + customer_id + ";");
                        if (outCome1_1 == 1) {
                            System.out.println("Customer with ID: " + customer_id + " is now removed!\n");
                        } else {
                            System.out.println("The customer id you have chosen is currently borrowing a book!");
                        }
                    } catch (Exception E) {
                        System.out.println("The customer id you have chosen is currently borrowing a book!");
                    }
                    break;
                default:
                    exit = true;
            }
        }
    }

    private static void list_table(String tableName) throws SQLException {
        rs = st.executeQuery("SELECT * FROM " + tableName + ";");

        int columnCount = rs.getMetaData().getColumnCount();
        String[] columnNames = new String[columnCount];
        for (int i = 0; i < columnCount; i++) {
            columnNames[i] = rs.getMetaData().getColumnName(i + 1);
        }

        for (String columnName : columnNames) {
            System.out.print(PadRight(columnName));
        }
        while (rs.next()) {
            System.out.println();
            for (String columnName : columnNames) {
                String value = rs.getString(columnName);

                if (value == null) {
                    value = "null";
                }

                System.out.print(PadRight(value));
            }
        }
        System.out.println("");
    }

    private static String PadRight(String string) {
        int totalStringLength = 30;
        int charsToPadd = totalStringLength - string.length();

        if (string.length() >= totalStringLength) {
            return string;
        }

        StringBuilder stringBuilder = new StringBuilder(string);
        for (int i = 0; i < charsToPadd; i++) {
            stringBuilder.append(" ");
        }

        return stringBuilder.toString();
    }

    private static void printTable(String sql) throws SQLException {
        rs = st.executeQuery(sql);
        int columnCount = rs.getMetaData().getColumnCount();
        String[] columnNames = new String[columnCount];
        for (int i = 0; i < columnCount; i++) {
            columnNames[i] = rs.getMetaData().getColumnName(i + 1);
        }

        for (String columnName : columnNames) {
            System.out.print(PadRight(columnName));
        }

        while (rs.next()) {
            System.out.println();
            for (String columnName : columnNames) {
                String value = rs.getString(columnName);

                if (value == null) {
                    value = "null";
                }

                System.out.print(PadRight(value));
            }
        }
        System.out.println();
    }

    private static void space() {
        System.out.println("");
        System.out.println("");
    }
}
