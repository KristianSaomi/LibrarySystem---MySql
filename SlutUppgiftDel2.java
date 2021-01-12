package slutuppgiftdel2;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
// MongoCursor har metoderna next och hasNext. Används med while-loop
import com.mongodb.client.MongoCursor;
// MongoIterable harinte next och hasNext. Kan itereras med for.
import com.mongodb.client.MongoIterable;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.result.DeleteResult;
import org.bson.Document;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import static com.mongodb.client.model.Filters.eq;
import com.mongodb.client.result.UpdateResult;
import java.util.Scanner;

public class SlutUppgiftDel2 {

    private static MongoClient mongoClient = null;
    private static MongoCollection<Document> libraryCollection = null;
    static Scanner sc = new Scanner(System.in);
    private static Document doc1 = null;

    public static void main(String[] args) {

        try {

            connectToDb();

            if (mongoClient == null) {
                System.out.println("Connection failed!");
                return;
            }

            printDatabases();

            getAuthorCollection();

            if (libraryCollection == null) {
                System.out.println("Connection failed!");
                return;
            }
            menu();

        } catch (Exception ex) {
            System.out.println("Your selection could not be made, follow the instructions");
        } finally {
            mongoClient.close();
        }
    }

    private static void connectToDb() {

        Logger.getLogger("org.mongodb.driver").setLevel(Level.WARNING);

        mongoClient = new MongoClient("localhost", 27017);

        System.out.println("Connection succeed");
    }

    private static void printDatabases() {
        MongoIterable<String> databaseNames = mongoClient.listDatabaseNames();
        System.out.println("--------------------------------------------------------------");
        System.out.println("Database:");
        for (String name : databaseNames) {
            System.out.println(name);
        }
    }

    private static void getAuthorCollection() {
        // Create connection to database libraryStore that exists on the connected server
        // If there is no one, No Mongo creates it once insertion is made!
        MongoDatabase mongoDB = mongoClient.getDatabase("libraryStore");

        // Searth up all collection in the database libraryStore and print to console
        System.out.println("--------------------------------------------------------------");
        System.out.println("Collections in database author:");
        MongoIterable<String> authorcollections = mongoDB.listCollectionNames();
        for (String name : authorcollections) {
            System.out.println(name);
        }

        // Connect to the collection "Author"
        // If there is no one, No Mongo creates it once insertion is made!
        libraryCollection = mongoDB.getCollection("author");

        System.out.println("Connection to the collection " + libraryCollection.getNamespace() + " succeed!");
        System.out.println("--------------------------------------------------------------");

    }

    private static void menu() {
        boolean exit = false;
        while (!exit) {
            System.out.println("Make a decision: ");
            System.out.println("\n1) Insert a book");
            System.out.println("\n2) Show all authors");
            System.out.println("\n3) List Author creation");
            System.out.println("\n4) Update function");
            System.out.println("\n5) Delete function");
            System.out.println("\n0) Exit");
            int choice = sc.nextInt();
            sc.nextLine();

            switch (choice) {
                case 1:
                    libraryInformation();
                    break;
                case 2:
                    showAuthorCollection();
                    break;
                case 3:
                    findAuthorCreation();
                    break;
                case 4:
                    updateFunction();
                    break;
                case 5:
                    deleteFunction();
                    break;
                case 0:
                    System.out.println("See you next time!");
                    exit = true;
                    break;
                default:
                    System.out.println("Wrong input, please try again.\n");
            }
        }
    }

    private static void libraryInformation() {
        // Create an BSON-Document with specific structure
        System.out.println("--------------------------------------------------------------");
        System.out.println("Book title");
        String bookName = sc.nextLine();
        System.out.println("Whats the book grade 1-5?");
        String stars = sc.nextLine();
        System.out.println("Whats the release year for the book?");
        int year = sc.nextInt();
        sc.nextLine();
        System.out.println("How many authors does the book have? \n1 or 2?");
        int x = sc.nextInt();
        sc.nextLine();
        try {
            switch (x) {
                case 1: {
                    System.out.println("Book author: ");
                    String author1 = sc.nextLine();
                    doc1 = new Document("Book title", bookName)
                            .append("Book grade", stars)
                            .append("Book author", Arrays.asList(author1))
                            .append("Book release year", year);
                    break;
                }
                case 2: {
                    System.out.println("Book author one: ");
                    String author1 = sc.nextLine();
                    System.out.println("Book author two: ");
                    String author2 = sc.nextLine();
                    doc1 = new Document("Book title", bookName)
                            .append("Book grade", stars)
                            .append("Book author", Arrays.asList(author1, author2))
                            .append("Book release year", year);
                    break;
                }
                default:
                    System.out.println("Please choose a valid option");
                    libraryInformation();
                    break;
            }
            List<Document> documents = new ArrayList<Document>();
            documents.add(doc1);
            //libraryCollection.insertMany(documents);
            libraryCollection.insertOne(doc1);
            // Message how many insertions been added to the library
            System.out.println("" + documents.size() + " books has been added to the library");
            System.out.println("--------------------------------------------------------------");
        } catch (Exception e) {
            System.out.println("Something went wrong, please follow the instructions and try again!");
        }
    }

    private static void showAuthorCollection() {
        showAuthor();
    }

    private static void showAuthor() {
        MongoCursor<Document> selectCursor = libraryCollection.find().iterator();

        while (selectCursor.hasNext()) {
            Document doc = selectCursor.next();
            ArrayList<Object> showAuthor = new ArrayList<>(doc.values());
            System.out.println("Book title: " + showAuthor.get(1));
            System.out.println("Book grade: " + showAuthor.get(2));
            System.out.println("Book author: " + showAuthor.get(3));
            System.out.println("Book release year: " + showAuthor.get(4));
            System.out.println("--------------------------------------------------------------");
        }
    }

    private static void updateFunction() {
        System.out.println("--------------------------------------------------------------");
        System.out.println("What would you like to update: ");
        System.out.println("\n1) Book author");
        System.out.println("\n2) Book title");
        System.out.println("\n3) Book grade");
        System.out.println("\n4) Book release year");
        System.out.println("\n0) Back to menu");
        int choice = sc.nextInt();
        sc.nextLine();

        boolean exit = false;
        {
            switch (choice) {
                case 1:
                    showAuthor();
                    updateAuthorMenu();
                    break;
                case 2:
                    showAuthor();
                    updateBookTitle();
                    break;
                case 3:
                    showAuthor();
                    updateBookGrade();
                    break;
                case 4:
                    showAuthor();
                    updateBookReleaseYear();
                    break;
                case 0:
                    exit = true;
                    break;
                default:
                    System.out.println("Please choose a valid option");
                    updateFunction();
                    break;
            }
        }
    }

    private static void updateAuthorMenu() {
        System.out.println("Does the book have 1 or 2 book authors?");
        int howmanyAuthors = sc.nextInt();
        sc.nextLine();

        switch (howmanyAuthors) {
            case 1:
                updateoneBookAuthor();
                break;
            case 2:
                updateMenu2();
                break;
        }
    }

    private static void updateoneBookAuthor() {
        System.out.println("------------------------------------------------------------------");
        System.out.println("| WARNING: Make sure you choose a book with only one book author! |  ");
        System.out.println("|            else the secend one will be deleteed                 |");
        System.out.println("------------------------------------------------------------------");
        System.out.println("\nWhich 'book author' would you like to update name for?");
        String bookAuthor = sc.nextLine();
        System.out.println("What would you like to change to?");
        String newBookAuthor = sc.nextLine();

        Document query = new Document("Book author", bookAuthor);
        MongoCursor<Document> SearchBookAuthor = libraryCollection.find(eq("Book author", bookAuthor)).iterator();
       
        Document setData2 = new Document("Book author", newBookAuthor);
        Document update = new Document("$set", setData2);

        UpdateResult result = libraryCollection.updateOne(query, update);
        if (SearchBookAuthor.hasNext()) {
            System.out.println("Update done successful!");
            System.out.println("--------------------------------------------------------------");
        } else {
            System.out.println("Update failed, the program is key sensitivity make sure the spelling and key sensitivity is correctly");
        }

    }

    private static void updateMenu2() {
        System.out.println("Would you like to update both '2' authors or only one '1' of them?");
        int amountOfAuthorToUpdate = sc.nextInt();
        sc.nextLine();
        switch (amountOfAuthorToUpdate) {
            case 1:
                updateOfOneAuthor();
                break;
            case 2:
                updateOfBothAuthors();
                break;
            default:
                System.out.println("You need to follow the instructions!");
        }
    }

    private static void updateOfOneAuthor() {
        System.out.println("------------------------------------------------------------------");
        System.out.println("|   WARNING: Make sure you choose a book with two book authors!   |  ");
        System.out.println("------------------------------------------------------------------");
        System.out.println("Which author would you like to update");
        System.out.println("Enter the name of the 'book author' you would like to update name for?");
        String bookAuthor = sc.nextLine();

        Document query = new Document("Book author", bookAuthor);
        MongoCursor<Document> SearchBookAuthor = libraryCollection.find(eq("Book author", bookAuthor)).iterator();
        if (SearchBookAuthor.hasNext()) {

            System.out.println("What would you like to change to?");
            String newBookAuthor = sc.nextLine();
            System.out.println("Please enter the name of the second author of this book");
            String bookAuthor2 = sc.nextLine();

            Document query2 = new Document("Book author", bookAuthor2);
            MongoCursor<Document> SearchBookAuthor2 = libraryCollection.find(eq("Book author", bookAuthor2)).iterator();
            if (SearchBookAuthor2.hasNext()) {
                Document setData = new Document("Book author", Arrays.asList(bookAuthor2, newBookAuthor));
                Document update = new Document("$set", setData);
                libraryCollection.updateOne(query, update);
                System.out.println("Update done successful!");
                System.out.println("--------------------------------------------------------------");

            } else {
                System.out.println("You have entered the wrong name for the second author\nPlease try again! ");
                System.out.println("Update failed, the program is key sensitive make sure the spelling and key sensitivity is correctly");

            }
        } else {
            System.out.println("You have entered the wrong name for the author you want to change\nPlease try again! ");
            System.out.println("Update failed, the program is key sensitive make sure the spelling and key sensitivity is correctly");

        }
    }

    private static void updateOfBothAuthors() {
        System.out.println("------------------------------------------------------------------");
        System.out.println("|    WARNING: Make sure you choose a book with two book author!   |  ");
        System.out.println("------------------------------------------------------------------");
        System.out.println("Enter the name of the first 'book author' you would like to update name for?");
        String bookAuthor = sc.nextLine();

        Document query = new Document("Book author", bookAuthor);
        MongoCursor<Document> SearchBookAuthor = libraryCollection.find(eq("Book author", bookAuthor)).iterator();

        if (SearchBookAuthor.hasNext()) {
            System.out.println("What would you like to change to?");
            String newBookAuthor = sc.nextLine();

            System.out.println("Enter the name of the second 'book author' you would like to update name for?");
            String bookAuthor2 = sc.nextLine();

            Document query2 = new Document("Book author", bookAuthor2);
            MongoCursor<Document> SearchBookAuthor2 = libraryCollection.find(eq("Book author", bookAuthor2)).iterator();
            if (SearchBookAuthor2.hasNext()) {
                System.out.println("What would you like to change to?");
                String newBookAuthor2 = sc.nextLine();

                Document setData = new Document("Book author", Arrays.asList(newBookAuthor, newBookAuthor2));
                Document update = new Document("$set", setData);

                libraryCollection.updateOne(query, update);

                System.out.println("Update done successful!");
                System.out.println("--------------------------------------------------------------");

            } else {
                System.out.println("");
                System.out.println("You have entered the wrong name for the second author you want to change\nPlease try again! ");
                System.out.println("Update failed, the program is key sensitive make sure the spelling and key sensitivity is correctly");
                System.out.println("");
            }
        } else {
            System.out.println("");
            System.out.println("You have entered the wrong name for the first author you want to change\nPlease try again! ");
            System.out.println("Update failed, the program is key sensitive make sure the spelling and key sensitivity is correctly");
            System.out.println("");

        }
    }

    private static void updateBookTitle() {
        System.out.println("Which 'book title' would you like to update?");
        String bookTitle = sc.nextLine();
        System.out.println("What would you like to change to?");
        String newBookTitle = sc.nextLine();

        Document query2 = new Document("Book title", bookTitle);
        MongoCursor<Document> SearchBookTitle = libraryCollection.find(eq("Book title", bookTitle)).iterator();

        Document setData2 = new Document("Book title", newBookTitle);
        Document update2 = new Document("$set", setData2);

        libraryCollection.updateOne(query2, update2);
        if (SearchBookTitle.hasNext()) {
            System.out.println("Update done successful!");
            System.out.println("--------------------------------------------------------------");

        } else {
            System.out.println("Update failed, the program is key sensitivity make sure the spelling and key sensitivity is correctly");
        }
    }

    private static void updateBookGrade() {
        System.out.println("Enter the 'book title' you would like to update [grade] for?");
        String bookTitle1 = sc.nextLine();
        System.out.println("What grade would you like to change to?");
        String newBookGrade = sc.nextLine();

        Document query3 = new Document("Book title", bookTitle1);
        MongoCursor<Document> SearchBookTitle = libraryCollection.find(eq("Book title", bookTitle1)).iterator();

        Document setData3 = new Document("Book grade", newBookGrade);
        Document update3 = new Document("$set", setData3);

        libraryCollection.updateOne(query3, update3);
        if (SearchBookTitle.hasNext()) {
            System.out.println("Update done successful!");
            System.out.println("--------------------------------------------------------------");

        } else {
            System.out.println("Update failed, the program is key sensitivity make sure the spelling and key sensitivity is correctly");
        }
    }

    private static void updateBookReleaseYear() {
        System.out.println("Enter the 'book title' you would like to update [release year] for?");
        String bookTitle2 = sc.nextLine();
        System.out.println("What release year would you like to change to?");
        String newBookReleaseYear = sc.nextLine();

        Document query4 = new Document("Book title", bookTitle2);
        MongoCursor<Document> SearchBookTitle = libraryCollection.find(eq("Book title", bookTitle2)).iterator();

        Document setData4 = new Document("Book release year", newBookReleaseYear);
        Document update4 = new Document("$set", setData4);

        libraryCollection.updateOne(query4, update4);
        if (SearchBookTitle.hasNext()) {
            System.out.println("\nUpdate done successful!\n");
            System.out.println("--------------------------------------------------------------");

        } else {
            System.out.println("\nUpdate failed, the program is key sensitivity make sure the spelling and key sensitivity is correctly!\n");
        }
    }

    private static void deleteFunction() {
        System.out.println("--------------------------------------------------------------");
        System.out.println("What would you like to delete: ");
        System.out.println("\n1) Book author");
        System.out.println("\n2) Book title");
        System.out.println("\n0) Back to menu");
        int choice = sc.nextInt();
        sc.nextLine();

        boolean exit = false;

        switch (choice) {
            case 1:
                showAuthor();
                deleteFunctionAuthor();
                break;
            case 2:
                showAuthor();
                deleteFunctionTitle();
                break;
            case 0:
                exit = true;
                break;
            default:
                System.out.println("Please choose a valid option");

        }

    }

    private static void deleteFunctionAuthor() {
        System.out.println("Which 'book author' would you like to delete?");
        String bookAuthor = sc.nextLine();
        MongoCursor<Document> SearchBookAuthor = libraryCollection.find(eq("Book author", bookAuthor)).iterator();
        if (SearchBookAuthor.hasNext()) {
            DeleteResult deleteResult1 = libraryCollection.deleteMany(eq("Book author", bookAuthor));
            System.out.println("\nDelete done successful!\n");
            System.out.println("--------------------------------------------------------------");

        } else {
            System.out.println("\nDelete failed, the program is key sensitivity make sure the spelling and key sensitivity is correctly!\n");

        }
    }

    private static void deleteFunctionTitle() {
        System.out.println("Which 'book title' would you like to delete?");
        String bookTitle = sc.nextLine();
        MongoCursor<Document> SearchBookTitle = libraryCollection.find(eq("Book title", bookTitle)).iterator();
        if (SearchBookTitle.hasNext()) {
            DeleteResult deleteResult2 = libraryCollection.deleteMany(eq("Book title", bookTitle));
            System.out.println("\nDelete done successful!\n");
            System.out.println("--------------------------------------------------------------");

        } else {
            System.out.println("\nDelete failed, the program is key sensitivity make sure the spelling and key sensitivity is correctly!\n");
        }
    }

    private static void findAuthorCreation() {
        // Med detta kommando kan du söka upp poster med parametrar.
        // Här ett enkelt som söker upp alla dokument där namnet är "Birch"
        System.out.println("");
        showAuthor();
        System.out.println("");
        System.out.println("Which author would you like to searth on?");
        System.out.println("OBS!: Key-sensetive");
        String x = sc.nextLine();
        MongoCursor<Document> SearchAuthor = libraryCollection.find(eq("Book author", x)).iterator();
        System.out.println("");
        if (SearchAuthor.hasNext()) {
            System.out.println("");
            System.out.println("--------------------------------------------------------------");
            System.out.println("We'v found the following books by: " + x);
            System.out.println("");
        } else {
            System.out.println("Anfortunatly we havent found any books by the name: " + x + " \nWould you like to try again with a different name? \n--yes or no--");
            String tryOption = sc.nextLine();
            if ("yes".equalsIgnoreCase(tryOption)) {
                findAuthorCreation();
            }
        }

        // Skriv ut resutlatet
        while (SearchAuthor.hasNext()) {
            Document doc = SearchAuthor.next();
            ArrayList<Object> authors = new ArrayList<>(doc.values());
            System.out.println("Title: " + authors.get(1) + "\n" + "Grade: " + authors.get(2) + "\n" + "Release year: " + authors.get(4) + "\n\n\n");
        }
        System.out.println("--------------------------------------------------------------");
        System.out.println("");
    }

}