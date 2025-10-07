import java.util.Date;
import java.util.Scanner;

class Book {

    public String title, author;
    public int totalCopies, availableCopies;
    // Constructor for class Book.
    public Book(String title, String author, int totalCopies) {
        this.title = title;
        this.author = author;
        this.totalCopies = totalCopies;
        this.availableCopies = totalCopies;
    }
    // this method substract 1 when a book is borrowed.
    public boolean borrowBook() {
        if (availableCopies > 0) {
            availableCopies--;
            return true;
        }
        return false;
    }
    // this method adds 1 when a book i returned.
    public void returnBook() {
        if (availableCopies < totalCopies) {
            availableCopies++;
        }
    }
    /*
    this method gives the details about the book its title, author, and 
    available copies.
    */
    public String getDetails() {
        return title + ", " + author + ", " + availableCopies + " copies.";
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }
}

class User {

    private String name;
    private String email;
    // a person can only borrow 3 books.
    private Book[] borrowedBooks = new Book[3];
    private int borrowCount = 0;
    // Constructor for class User.
    public User(String name, String email) {
        this.name = name;
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }
    /* this method lets you borrow a book 
    if you have borrowed less than 3 books
    */
    public boolean borrowBook(Book book) {
        if (borrowCount < 3 && book.borrowBook()) {
            borrowedBooks[borrowCount++] = book;
            return true;
        }
        return false;
    }
    /*
    this method replaces the returned book
    with the last book in the borrowedBooks array.
    */
    public boolean returnBook(Book book) {
        for (int i = 0; i < borrowCount; i++) {
            if (borrowedBooks[i] == book) {
                book.returnBook();
                borrowedBooks[i] = borrowedBooks[--borrowCount];
                return true;
            }
        }
        return false;
    }
}

class Transaction {

    private User user;
    private Book book;
    // the types are borrow or return.
    private String type;
    // to keep track of the date of the transaction.
    private Date date;
    // Constructor for class Transaction.
    public Transaction(User user, Book book, String type) {
        this.user = user;
        this.book = book;
        this.type = type;
        this.date = new Date();
    }
    /* 
    this method gives the details about the type if it is borrow or return, 
    the detail of the book, the name of the borrower 
    and the date of the date of the transaction.
    */
    public String getDescription() {
        return type + " - " + book.getDetails() + " by " + user.getName() + " on " + date;
    }
}

class Library {
    // starting with 10 shelves and 4 books each shelf.
    private static final int shelfCapacity = 4;
    private static final int initialShelves = 10;
    private Book[][] shelves = new Book[initialShelves][shelfCapacity];
    private int currentShelf = 0;
    private int booksOnShelf = 0;
    private User[] users = new User[10];
    private int userCount = 0;
    private Transaction[] transactions = new Transaction[100];
    private int transactionCount = 0;
    // this is the method adds the books to the library.
    public void addBook(String title, String author, int totalCopies) {
        if (title == null || title.length() == 0 || author == null || author.length() == 0 || totalCopies <= 0) {
            System.out.println("Invalid book details. Please try again.");
            return;
        }
        /* this loop is to make sure that there is no books 
        with the same title and author.
        */
        // this variable holds the upper limit for the loop
        int limit; 
        for (int i = 0; i <= currentShelf; i++) {
            if (i == currentShelf) {
                limit = booksOnShelf;
            } else {
                limit = 4;
            }
            for (int j = 0; j < limit; j++) {
                if (shelves[i][j] != null
                        && shelves[i][j].getTitle().equalsIgnoreCase(title)
                        && shelves[i][j].getAuthor().equalsIgnoreCase(author)) {
                    System.out.println("Book already exists in the library. the entered copies will be added to the same book.");
                    shelves[i][j].availableCopies += totalCopies;
                    return;
                }
            }
        }

        /* this if statement checks if a new shelf is needed and then adds 10 more 
        shelves and copies the previous 10 shelves and places them in the first 10 
        shelves after that the new books continue with the new shelves.
         */
        if (booksOnShelf == 4) {
            if (currentShelf == shelves.length - 1) {
                Book[][] newShelves = new Book[shelves.length + 10][4];
                System.arraycopy(shelves, 0, newShelves, 0, shelves.length);
                shelves = newShelves;
                System.out.println("10 new shelves were built.");
            }
            // moves to the next shelf because the current shelf is full.
            currentShelf++;
            booksOnShelf = 0;
        }

        // here we add the book to the shelf.
        shelves[currentShelf][booksOnShelf++] = new Book(title, author, totalCopies);
        // and here it displays the details of the added book.
        System.out.println("Book '" + title + "' by " + author + " has been added with " + totalCopies + " copies.");
    }
    // this method is where users are registered.
    public void registerUser(String name, String email) {
        // 2 users can not have the same email.
        for (int i = 0; i < userCount; i++) {
            if (users[i].getEmail().equalsIgnoreCase(email)) {
                System.out.println("A user with this email already exists.");
                return;
            }
        }
        // if there is more than 10 users 10 more well be added.
        if (userCount == users.length) {
            User[] newUsers = new User[users.length + 10];
            System.arraycopy(users, 0, newUsers, 0, users.length);
            users = newUsers;
        }
        // adding new users.
        users[userCount++] = new User(name, email);
        System.out.println("User '" + name + "' has been registered.");
    }
    // this method finds the books after checking all elements in the array.
    public Book findBook(String title, String author) {
        int limit;
        for (int i = 0; i <= currentShelf; i++) {
            if (i == currentShelf) {
                limit = booksOnShelf;
            } else {
                limit = 4;
            }
            for (int j = 0; j < limit; j++) {
                if (shelves[i][j].getTitle().equalsIgnoreCase(title) && shelves[i][j].getAuthor().equalsIgnoreCase(author)) {
                    return shelves[i][j];
                }
            }
        }
        System.out.println("Book not found.");
        return null;
    }
    // this method finds the users after checking all elements in the array.
    public User findUser(String email) {
        for (int i = 0; i < userCount; i++) {
            if (users[i].getEmail().equalsIgnoreCase(email)) {
                return users[i];
            }
        }
        System.out.println("User not found.");
        return null;
    } 
    //this method checks the ability to borrow a book, then it does the work. 
    public boolean borrowBook(String email, String title, String author) {
        User user = findUser(email);
        Book book = findBook(title, author);
          if (user != null && book != null && user.borrowBook(book)) {
                if (transactionCount == transactions.length) {
                    Transaction[] newTransactions = new Transaction[transactions.length + 100];
                    System.arraycopy(transactions, 0, newTransactions, 0, transactions.length);
                    transactions = newTransactions;
            }
            transactions[transactionCount++] = new Transaction(user, book, "Borrow");
            System.out.println("Book borrowed successfully! Remaining copies: " + book.availableCopies);

            return true;
        }
        return false;
    }
    // this method checks the ability to return a book, then it does the work.
    public boolean returnBook(String email, String title, String author) {
        User user = findUser(email);
        Book book = findBook(title, author);
        if (user != null && book != null && user.returnBook(book)) {
            if (transactionCount == transactions.length) {
                Transaction[] newTransactions = new Transaction[transactions.length + 100];
                System.arraycopy(transactions, 0, newTransactions, 0, transactions.length);
                transactions = newTransactions;
            }
            transactions[transactionCount++] = new Transaction(user, book, "Return");
            System.out.println("Book returned successfully! Remaining copies: " + book.availableCopies);
            return true;
        }
        return false;
    }
    // this method lists all entered books and their details.
    public void listBooks() {
        if (currentShelf == 0 && booksOnShelf == 0) {
            System.out.println("No books available in the library.");
            return;
        }
        int limit;
        for (int i = 0; i <= currentShelf; i++) {
            if (i == currentShelf) {
                limit = booksOnShelf; // For the last shelf, limit is the number of books
            } else {
                limit = 4; // For other shelves, the limit is the full capacity of the shelf
            }
            for (int j = 0; j < limit; j++) {
                System.out.println(shelves[i][j].getDetails());
            }
        }
    }
    // this method
    public void listTransactions() {
        if (transactionCount == 0) {
            System.out.println("No transactions recorded.");
            return;
        }
        for (int i = 0; i < transactionCount; i++) {
            System.out.println(transactions[i].getDescription());
        }
    }
}

public class Project {

    public static void main(String[] args) {
        Library library = new Library();
        Scanner scanner = new Scanner(System.in);

        for (;;) {
            System.out.println("\nLibrary Management System: ");
            System.out.println("1. Add a New Book");
            System.out.println("2. Register a New User");
            System.out.println("3. Borrow a Book");
            System.out.println("4. Return a Book");
            System.out.println("5. Display All Books");
            System.out.println("6. View Transactions");
            System.out.println("7. Exit");

            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    System.out.print("Enter title: ");
                    String title = scanner.nextLine();
                    System.out.print("Enter author: ");
                    String author = scanner.nextLine();
                    System.out.print("Enter total copies: ");
                    int copies = scanner.nextInt();
                    library.addBook(title, author, copies);
                    break;

                case 2: 
                    System.out.print("Enter name: ");
                    String name = scanner.nextLine();
                    System.out.print("Enter email: ");
                    String email = scanner.nextLine();
                    library.registerUser(name, email);
                    break;

                case 3:
                    System.out.print("Enter user email: ");
                    String borrowerEmail = scanner.nextLine();
                    System.out.print("Enter book title: ");
                    String borrowTitle = scanner.nextLine();
                    System.out.print("Enter book author: ");
                    String borrowAuthor = scanner.nextLine();
                    if (library.borrowBook(borrowerEmail, borrowTitle, borrowAuthor)) {
                        System.out.println("Book '" + borrowTitle + "' has been borrowed.");
                    } else {
                        System.out.println("Could not borrow book. Check availability or user details.");
                    }
                    break;

                case 4:
                    System.out.print("Enter user email: ");
                    String returnEmail = scanner.nextLine();
                    System.out.print("Enter book title: ");
                    String returnTitle = scanner.nextLine();
                    System.out.print("Enter book author: ");
                    String returnAuthor = scanner.nextLine();
                    if (library.returnBook(returnEmail, returnTitle, returnAuthor)) {
                        System.out.println("Book '" + returnTitle + "' has been returned");
                    } else {
                        System.out.println("Could not return book. Check user and book details.");
                    }
                    break;

                case 5:
                    library.listBooks();
                    break;

                case 6:
                    library.listTransactions();
                    break;

                case 7:
                    System.out.println("Program exited successfully.");
                    scanner.close();
                    return;

                default:
                    System.out.println("Wrong input, try again.");
            }
        }
    }
}   