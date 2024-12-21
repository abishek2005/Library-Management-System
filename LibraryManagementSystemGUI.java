import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
// Main GUI Class
public class LibraryManagementSystemGUI extends JFrame {
    private Library library;
    private JTextArea outputArea;
    public LibraryManagementSystemGUI() {
        library = new Library(); // Initialize Library
        setTitle("Library Management System");
        setSize(600, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        // Main Menu Panel with Access and Admin Login buttons
        JPanel mainMenuPanel = new JPanel(new GridLayout(2, 1, 10, 10));
        JButton accessLibraryButton = new JButton("Access Library");
        JButton loginAsAdminButton = new JButton("Login as Admin");
        mainMenuPanel.add(accessLibraryButton);
        mainMenuPanel.add(loginAsAdminButton);
        add(mainMenuPanel, BorderLayout.CENTER);
        // Action listener for accessing library as a regular user
        accessLibraryButton.addActionListener(e -> openLibraryManagementWindow(false)); // User panel
        // Action listener for admin login button
        loginAsAdminButton.addActionListener(e -> showAdminLoginDialog());
    }
    // Main method to launch the program
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            LibraryManagementSystemGUI gui = new LibraryManagementSystemGUI();
            gui.setVisible(true);
        });
    }
    // Shows the Admin Login dialog
    private void showAdminLoginDialog() {
        JDialog loginDialog = new JDialog(this, "Admin Login", true);
        loginDialog.setSize(300, 150);
        loginDialog.setLayout(new GridLayout(3, 2, 10, 10));
        JLabel usernameLabel = new JLabel("Username:");
        JTextField usernameField = new JTextField();
        JLabel passwordLabel = new JLabel("Password:");
        JPasswordField passwordField = new JPasswordField();
        JButton loginButton = new JButton("Login");
        loginDialog.add(usernameLabel);
        loginDialog.add(usernameField);
        loginDialog.add(passwordLabel);
        loginDialog.add(passwordField);
        loginDialog.add(new JLabel()); // Empty cell for alignment
        loginDialog.add(loginButton);
        // Admin login button action
        loginButton.addActionListener(e -> {
            String username = usernameField.getText();
            String password = new String(passwordField.getPassword());
            // Check credentials
            if (username.equals("admin") && password.equals("admin")) {
                loginDialog.dispose();
                openLibraryManagementWindow(true); // Open admin panel
            } else {
                JOptionPane.showMessageDialog(loginDialog, "Invalid login. Please try again.");
            }
        });
        loginDialog.setLocationRelativeTo(this);
        loginDialog.setVisible(true);
    }
    // Opens the main Library Management interface
    private void openLibraryManagementWindow(boolean isAdmin) {
        setTitle("Library Management System");
        setSize(600, 500);
        getContentPane().removeAll();
        setLayout(new BorderLayout());
        outputArea = new JTextArea();
        outputArea.setEditable(false);
        add(new JScrollPane(outputArea), BorderLayout.CENTER);
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(0, 1, 5, 5));
        if (isAdmin) {
            JButton addMemberButton = new JButton("Add New Member");
            JButton removeMemberButton = new JButton("Remove Member");
            JButton addBookButton = new JButton("Add New Book");
            JButton removeBookButton = new JButton("Remove Book");
            JButton renewMemberButton = new JButton("Renew Membership");
            JButton viewMembersButton = new JButton("View All Members");
            JButton viewBooksButton = new JButton("View Available Books");
            buttonPanel.add(addMemberButton);
            buttonPanel.add(removeMemberButton);
            buttonPanel.add(addBookButton);
            buttonPanel.add(removeBookButton);
            buttonPanel.add(renewMemberButton);
            buttonPanel.add(viewMembersButton);
            buttonPanel.add(viewBooksButton);
            // Admin actions
            addMemberButton.addActionListener(e -> addNewMember());
            addBookButton.addActionListener(e -> addNewBook());
            removeBookButton.addActionListener(e -> removeBook());
            viewBooksButton.addActionListener(e -> viewAvailableBooks());
            renewMemberButton.addActionListener(e -> renewMembership());
            viewMembersButton.addActionListener(e -> viewAllMembers());
            removeMemberButton.addActionListener(e -> removeMemberFromList());
        } else {
            // Regular user actions
            JButton borrowBookButton = new JButton("Borrow Book");
            JButton returnBookButton = new JButton("Return Book");
            JButton viewBooksButton = new JButton("View Available Books");
            buttonPanel.add(borrowBookButton);
            buttonPanel.add(returnBookButton);
            buttonPanel.add(viewBooksButton);
            borrowBookButton.addActionListener(e -> borrowBook());
            returnBookButton.addActionListener(e -> returnBook());
            viewBooksButton.addActionListener(e -> viewAvailableBooks());
        }
        JButton backButton = new JButton("Back to Main Menu");
        backButton.addActionListener(e -> backToMainMenu());
        add(backButton, BorderLayout.SOUTH);
        add(buttonPanel, BorderLayout.WEST);
        revalidate();
        repaint();
    }
    private void addNewMember() {
        String name = JOptionPane.showInputDialog(this, "Enter member's name:");
        if (name != null && !name.isEmpty()) {
            Member newMember = new Member(name);
            library.addMember(newMember);
            outputArea.append("New member added: " + newMember.getName() + " with Library ID: " + newMember.getLibraryId() + "\n");
        } else {
            JOptionPane.showMessageDialog(this, "Member name cannot be empty.");
        }
    }
    private void viewAllMembers() {
        outputArea.setText("");
        outputArea.append("All Members:\n");
        for (Member member : library.getMembers()) {
            outputArea.append(member.toString() + "\n");
        }
    }
    private void removeMemberFromList() {
        String memberIdStr = JOptionPane.showInputDialog(this, "Enter Member ID to remove:");
        try {
            int memberId = Integer.parseInt(memberIdStr);
            library.removeMember(memberId);
            outputArea.append("Member with ID " + memberId + " removed successfully.\n");
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Invalid Member ID. Please enter a valid number.");
        }
    }
    private void addNewBook() {
        String title = JOptionPane.showInputDialog(this, "Enter book title:");
        String author = JOptionPane.showInputDialog(this, "Enter book author:");
        if (title != null && !title.isEmpty() && author != null && !author.isEmpty()) {
            Book newBook = new Book(title, author);
            library.addBook(newBook);
            outputArea.append("Book added: " + title + " by " + author + "\n");
        } else {
            JOptionPane.showMessageDialog(this, "Both title and author are required.");
        }
    }
    private void removeBook() {
        String title = JOptionPane.showInputDialog(this, "Enter title of the book to remove:");
        if (title != null && !title.isEmpty()) {
            library.removeBook(title, outputArea);
        } else {
            JOptionPane.showMessageDialog(this, "Book title cannot be empty.");
        }
    }
    private void renewMembership() {
        String libraryIdStr = JOptionPane.showInputDialog(this, "Enter Library ID of member to renew membership:");
        try {
            int libraryId = Integer.parseInt(libraryIdStr);
            library.renewMember(libraryId);
            outputArea.append("Membership for member with ID " + libraryId + " has been renewed.\n");
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Invalid Member ID.");
        }
    }
    private void borrowBook() {
        String title = JOptionPane.showInputDialog(this, "Enter book title:");
        String identifier = JOptionPane.showInputDialog(this, "Enter member identifier (name or ID):");  
        if (title != null && !title.isEmpty() && identifier != null && !identifier.isEmpty()) {
            // Check if the identifier is a valid member (either name or ID)
            Member member = findMemberByIdentifier(identifier); 
            if (member != null) {
                library.borrowBook(title, member.getName(), outputArea);
            } else {
                JOptionPane.showMessageDialog(this, "You are not a member of the library.");
            }
        } else {
            JOptionPane.showMessageDialog(this, "Both title and member identifier are required.");
        }
    }  
    // Helper method to find a member by their name or ID
    private Member findMemberByIdentifier(String identifier) {
        try {
            // Check if the identifier is an integer (library ID)
            int memberId = Integer.parseInt(identifier);
            for (Member member : library.getMembers()) {
                if (member.getLibraryId() == memberId) {
                    return member;
                }
            }
        } catch (NumberFormatException e) {
            // If it's not an integer, treat it as a name
            for (Member member : library.getMembers()) {
                if (member.getName().equalsIgnoreCase(identifier)) {
                    return member;
                }
            }
        }
        return null; // If no member matches, return null
    }    
    private void returnBook() {
        String title = JOptionPane.showInputDialog(this, "Enter book title:");
        String identifier = JOptionPane.showInputDialog(this, "Enter member identifier (name or ID):");
        if (title != null && !title.isEmpty() && identifier != null && !identifier.isEmpty()) {
            library.returnBook(title, identifier, outputArea);
        } else {
            JOptionPane.showMessageDialog(this, "Both title and member identifier are required.");
        }
    }
    private void viewAvailableBooks() {
        outputArea.setText("");
        library.listAvailableBooks(outputArea);
    }
    private void backToMainMenu() {
        setTitle("Library Management System");
        getContentPane().removeAll();
        setLayout(new BorderLayout());
        JPanel mainMenuPanel = new JPanel(new GridLayout(2, 1, 10, 10));
        JButton accessLibraryButton = new JButton("Access Library");
        JButton loginAsAdminButton = new JButton("Login as Admin");
        mainMenuPanel.add(accessLibraryButton);
        mainMenuPanel.add(loginAsAdminButton);
        add(mainMenuPanel, BorderLayout.CENTER);
        accessLibraryButton.addActionListener(e -> openLibraryManagementWindow(false));
        loginAsAdminButton.addActionListener(e -> showAdminLoginDialog());
        revalidate();
        repaint();
    }
}
// Book Class
class Book {
    private String title;
    private String author;
    private boolean available;
    public Book(String title, String author) {
        this.title = title;
        this.author = author;
        this.available = true;
    }
    public String getTitle() {
        return title;
    }
    public String getAuthor() {
        return author;
    }
    public boolean isAvailable() {
        return available;
    }
    public void setAvailable(boolean available) {
        this.available = available;
    }
    @Override
    public String toString() {
        return title + " by " + author + (available ? " (Available)" : " (Borrowed)");
    }
}
// Member Class
class Member {
    private static int idCounter = 1000;  // Start from 1000
    private int libraryId;
    private String name;
    public Member(String name) {
        this.name = name;
        this.libraryId = idCounter++;
    }
    public int getLibraryId() {
        return libraryId;
    }
    public String getName() {
        return name;
    }
    @Override
    public String toString() {
        return "ID: " + libraryId + ", Name: " + name;
    }
}
// Library Class
class Library {
    private List<Book> books;
    private List<Member> members;
    public Library() {
        books = new ArrayList<>();
        members = new ArrayList<>();
    }
    public void addBook(Book book) {
        books.add(book);
    }
    public void removeBook(String title, JTextArea outputArea) {
        Book book = findBookByTitle(title);
        if (book != null) {
            books.remove(book);
            outputArea.append("Book removed: " + title + "\n");
        } else {
            outputArea.append("Book not found: " + title + "\n");
        }
    }
    public void addMember(Member member) {
        members.add(member);
    }
    public void removeMember(int libraryId) {
        members.removeIf(member -> member.getLibraryId() == libraryId);
    }
    public List<Member> getMembers() {
        return members;
    }

    public void borrowBook(String title, String identifier, JTextArea outputArea) {
        Book book = findBookByTitle(title);
        if (book != null && book.isAvailable()) {
            book.setAvailable(false);
            outputArea.append("Book borrowed: " + title + " by " + identifier + "\n");
        } else if (book == null) {
            outputArea.append("Book not found: " + title + "\n");
        } else {
            outputArea.append("Book is already borrowed: " + title + "\n");
        }
    }

    public void returnBook(String title, String identifier, JTextArea outputArea) {
        Book book = findBookByTitle(title);
        if (book != null && !book.isAvailable()) {
            book.setAvailable(true);
            outputArea.append("Book returned: " + title + " by " + identifier + "\n");
        } else if (book == null) {
            outputArea.append("Book not found: " + title + "\n");
        } else {
            outputArea.append("Book is already available: " + title + "\n");
        }
    }

    public void listAvailableBooks(JTextArea outputArea) {
        outputArea.append("Available Books:\n");
        for (Book book : books) {
            if (book.isAvailable()) {
                outputArea.append(book.toString() + "\n");
            }
        }
    }

    public void renewMember(int libraryId) {
        // In a real system, we would track membership dates, etc.
        // For simplicity, we'll just print that it's been renewed.
    }

    private Book findBookByTitle(String title) {
        for (Book book : books) {
            if (book.getTitle().equalsIgnoreCase(title)) {
                return book;
            }
        }
        return null;
    }
}
