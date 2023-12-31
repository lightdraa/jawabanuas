
package todolist052;

import java.sql.*;
import java.util.Scanner;

class Todo {
    private int id;
    private String todo;
    private String kategori;
    private String tanggalSelesai;
    private String status;

    public Todo() {}

    public Todo(String todo, String kategori, String tanggalSelesai, String status) {
        this.todo = todo;
        this.kategori = kategori;
        this.tanggalSelesai = tanggalSelesai;
        this.status = status;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setTodo(String todo) {
        this.todo = todo;
    }

    public void setKategori(String kategori) {
        this.kategori = kategori;
    }

    public void setTanggalSelesai(String tanggalSelesai) {
        this.tanggalSelesai = tanggalSelesai;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public String getTodo() {
        return todo;
    }

    public String getKategori() {
        return kategori;
    }

    public String getTanggalSelesai() {
        return tanggalSelesai;
    }

    public String getStatus() {
        return status;
    }
}

class TodoDATA {
    private Connection connection;

    public TodoDATA() throws SQLException {
        String url = "jdbc:mysql://localhost/todolist_052";
        String username = "root";
        String password = "";

        connection = DriverManager.getConnection(url, username, password);
    }

    public void tambahTodo(Todo todo) throws SQLException {
        String query = "INSERT INTO todolist (todo, kategori, tanggal_selesai, status) VALUES (?, ?, ?, ?)";

        PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
        statement.setString(1, todo.getTodo());
        statement.setString(2, todo.getKategori());
        statement.setString(3, todo.getTanggalSelesai());
        statement.setString(4, todo.getStatus());

        int affectedRows = statement.executeUpdate();

        if (affectedRows > 0) {
            ResultSet generatedKeys = statement.getGeneratedKeys();
            if (generatedKeys.next()) {
                int id = generatedKeys.getInt(1);
                todo.setId(id);
                System.out.println("Todo dengan ID " + id + " berhasil ditambahkan!");
            }
        }
    }

    public void tampilkanTodos() throws SQLException {
        String query = "SELECT * FROM todolist";

        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(query);

        while (resultSet.next()) {
            int id = resultSet.getInt("id");
            String todo = resultSet.getString("todo");
            String kategori = resultSet.getString("kategori");
            String tanggalSelesai = resultSet.getString("tanggal_selesai");
            String status = resultSet.getString("status");

            System.out.println("ID: " + id);
            System.out.println("Todo: " + todo);
            System.out.println("Kategori: " + kategori);
            System.out.println("Tanggal Selesai: " + tanggalSelesai);
            System.out.println("Status: " + status);
            System.out.println();
        }
    }

    public void updateTodoStatus(int id, String status) throws SQLException {
        String query = "UPDATE todolist SET status = ? WHERE id = ?";

        PreparedStatement statement = connection.prepareStatement(query);
        statement.setString(1, status);
        statement.setInt(2, id);

        int affectedRows = statement.executeUpdate();

        if (affectedRows > 0) {
            System.out.println("Todo dengan ID " + id + " berhasil diupdate!");
        } else {
            System.out.println("Todo dengan ID " + id + " tidak ditemukan.");
        }
    }

    public void hapusTodo(int id) throws SQLException {
        String query = "DELETE FROM todolist WHERE id = ?";

        PreparedStatement statement = connection.prepareStatement(query);
        statement.setInt(1, id);

        int affectedRows = statement.executeUpdate();

        if (affectedRows > 0) {
            System.out.println("Todo dengan ID " + id + " berhasil dihapus!");
        } else {
            System.out.println("Todo dengan ID " + id + " tidak ditemukan.");
        }
    }

    public void closeConnection() throws SQLException {
        connection.close();
        System.out.println("Koneksi ke database ditutup!");
    }
}

public class Todolist052 {
    public static void main(String[] args) {
        try {
            TodoDATA todoDATA = new TodoDATA();

            Scanner scanner = new Scanner(System.in);
            int pilihan = 0;

            while (pilihan != 5) {
                System.out.println("Menu:");
                System.out.println("1. Tambah Todo");
                System.out.println("2. Tampilkan TodoList");
                System.out.println("3. Update Status Todo");
                System.out.println("4. Hapus Todo");
                System.out.println("5. Keluar");
                System.out.print("Pilih menu (1-5): ");
                pilihan = scanner.nextInt();

                switch (pilihan) {
                    case 1:
                        System.out.print("Todo: ");
                        scanner.nextLine();
                        String todo = scanner.nextLine();
                        System.out.print("Kategori: ");
                        String kategori = scanner.nextLine();
                        System.out.print("Tanggal Selesai (YYYY-MM-DD): ");
                        String tanggalSelesai = scanner.nextLine();
                        System.out.print("Status: ");
                        String status = scanner.nextLine();

                        Todo newTodo = new Todo(todo, kategori, tanggalSelesai, status);
                        todoDATA.tambahTodo(newTodo);
                        break;
                    case 2:
                        todoDATA.tampilkanTodos();
                        break;
                    case 3:
                        System.out.print("ID Todo yang akan diupdate: ");
                        int idUpdate = scanner.nextInt();
                        scanner.nextLine();
                        System.out.print("Status baru: ");
                        String statusBaru = scanner.nextLine();

                        todoDATA.updateTodoStatus(idUpdate, statusBaru);
                        break;
                    case 4:
                        System.out.print("ID Todo yang akan dihapus: ");
                        int idHapus = scanner.nextInt();

                        todoDATA.hapusTodo(idHapus);
                        break;
                    case 5:
                        todoDATA.closeConnection();
                        break;
                    default:
                        System.out.println("Pilihan tidak valid!");
                }
            }

            scanner.close();
        } catch (SQLException e) {
            System.out.println("Terjadi kesalahan pada database: " + e.getMessage());
        }
    }
}
