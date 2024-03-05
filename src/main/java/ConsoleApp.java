import de.vandermeer.asciitable.AsciiTable;
import dao.Dao;

import javax.crypto.NoSuchPaddingException;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Objects;

public class ConsoleApp {
    private static final RegisterSystem rs = new RegisterSystem();
    private static final Dao<Movie> movieDao = new MovieDao("data" + File.separator + "movie.json");
    private static final Dao<Session> sessionDao = new SessionDao("data" + File.separator + "session.json");
    private static final BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

    public static void start() {
        // Запускаем наше приложение
        try {
            rs.Init("data" + File.separator + "key.txt", "data" + File.separator + "user.json");
            printRegisterStartPage();
        } catch (NoSuchPaddingException | NoSuchAlgorithmException | InvalidKeyException | InvalidAlgorithmParameterException e) {
            System.out.println("Oops... Something with cipher went wrong.");
        } catch (IOException e) {
            System.out.println("Oops... Probably you forgot to write a key.");
        }
    }

    private static void printRegisterStartPage() throws IOException {
        // Выводим начальное меню с входом и регистрацией
        while (true) {
            System.out.println("Hello, it is cinema managing system.");
            System.out.println("What you need to do?");
            System.out.println("Available options:");
            System.out.println("1 - Sign in");
            System.out.println("2 - Sign up");
            System.out.println("3 - Exit");
            System.out.println();
            System.out.print("You choice> ");

            String choice = reader.readLine();
            System.out.println();
            switch (choice) {
                case "1":
                    printSignInPage();
                    break;
                case "2":
                    printSignUpPage();
                    break;
                case "3":
                    System.out.println("Exiting...");
                    System.exit(0);
                    break;
                default:
                    System.out.println("Please choose one of available options");
            }
        }
    }

    private static void printSignUpPage() throws IOException {
        // Вывод интерфейса регистрации
        String choice = "";
        do {
            System.out.print("Please enter your name> ");
            String name = reader.readLine();
            if (name.isEmpty()) continue;
            System.out.println();
            System.out.print("Please enter your password> ");
            String password = reader.readLine();
            if (password.isEmpty()) continue;
            System.out.println();

            try {
                rs.addUser(name, password);
                System.out.println("You are successfully registered.");
                printMainMenu();
                break;
            }catch (IllegalArgumentException e) {
                System.out.println("User with such name is already registered.");
                System.out.println();
            }catch (Exception e) {
                System.out.println("Oops... Something with data went wrong.");
                System.out.println();
            }

            System.out.print("Print q to return to previous menu or something else to try again> ");
            choice = reader.readLine();
            System.out.println();
        } while (!Objects.equals(choice, "q"));
    }

    private static void printSignInPage() throws IOException {
        // Вывод интерфейса входа в аккаунт
        String choice;
        do {
            System.out.print("Please enter your name> ");
            String name = reader.readLine();
            System.out.println();
            System.out.print("Please enter your password> ");
            String password = reader.readLine();
            System.out.println();

            try {
                if (rs.findUser(name, password)) {
                    System.out.println("Access granted.");
                    printMainMenu();
                    break;
                } else {
                    System.out.println("Access denied.");
                }
            } catch (Exception e) {
                System.out.println("Oops... Something with data went wrong.");
                System.out.println();
            }

            System.out.print("Print q to return to previous menu or something else to try again> ");
            choice = reader.readLine();
            System.out.println();
        } while (!Objects.equals(choice, "q"));
    }

    private static void printMainMenu() throws IOException {
        // Вывод основаного меню и обработка ввода
        while (true) {
            System.out.println("What you need to do?");
            System.out.println("Available options:");
            System.out.println("1 - Add information about ticket sale");
            System.out.println("2 - Refund a ticket");
            System.out.println("3 - Punch a ticket");
            System.out.println("4 - Edit movie information");
            System.out.println("5 - Edit schedule");
            System.out.println("6 - Return to start page");
            System.out.print("You choice> ");

            String choice = reader.readLine();
            System.out.println();
            Session session;
            switch (choice) {
                case "1":
                    session = GetSessionFromUser();
                    if (session == null) {
                        printMainMenu();
                        break;
                    }

                    // Мы хотим внести информацию о продаже билета, т.е. изменить состояние места на продано
                    printChangingTicketStatePage(session, SeatState.SOLD);
                    break;
                case "2":
                    session = GetSessionFromUser();
                    if (session == null) {
                        printMainMenu();
                        break;
                    }

                    // Мы хотим вернуть билет, т.е. изменить состояние места на свободно
                    printChangingTicketStatePage(session, SeatState.FREE);
                    break;
                case "3":
                    session = GetSessionFromUser();
                    if (session == null) {
                        printMainMenu();
                        break;
                    }

                    // Мы хотим отметить билет, как уже использованный (пробитый), т.е. изменить состояние места на пробито
                    printChangingTicketStatePage(session, SeatState.PUNCHED);
                    break;
                case "4":
                    printEditingMoviesPage();
                    break;
                case "5":
                    printEditingSchedulePage();
                    break;
                case "6":
                    printRegisterStartPage();
                    break;
                default:
                    System.out.println("Please choose one of available options");
            }
        }
    }

    private static void printEditingSchedulePage() throws IOException {
        // Интерфейс изменения расписания кинотеатра
        while (true) {
            System.out.println("What you need to do?");
            System.out.println("Available options:");
            System.out.println("1 - Add session");
            System.out.println("2 - Delete session");
            System.out.println("3 - Return to main menu");
            System.out.println();
            System.out.print("You choice> ");

            String choice = reader.readLine();
            System.out.println();
            switch (choice) {
                case "1":
                    printSessionAddingPage();
                    break;
                case "2":
                    printDeletingSessionPage();
                    break;
                case "3":
                    System.out.println("Returning to main menu...");
                    System.out.println();

                    printMainMenu();
                    break;
                default:
                    System.out.println("Please choose one of available options");
            }
        }
    }

    private static void printDeletingSessionPage() {
        // Получаем от пользователя сеанс и удаляем его
        try {
            Session session = GetSessionFromUser();
            sessionDao.delete(session);
        } catch (IOException e) {
            System.out.println("Something with data went wrong.");
        }
    }

    private static void printSessionAddingPage() throws IOException {
        // Получаем от пользователя информацию о сеансе и сохраняем ее
        Movie movie = getMovieByUser();
        if (movie == null) {
            printMainMenu();
            return;
        }

        while (true) {
            System.out.print("Please enter a start time (yyyy-MM-dd HH:mm)> ");
            String timeStr;
            timeStr = reader.readLine();
            System.out.println();

            try {
                // Проверяем введеную строку, чтобы она соответствовала формату времени
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
                LocalDateTime.parse(timeStr, formatter);
                sessionDao.save(new Session(timeStr, movie));

                System.out.println("Session was added successfully.");
                System.out.println();
                break;
            } catch (IndexOutOfBoundsException e) {
                System.out.println("There is no seat with such coordinates.");
            } catch (DateTimeParseException e) {
                System.out.println("Incorrect time format.");
            } catch (IOException e) {
                System.out.println("Something with data went wrong.");
            }
        }
    }

    private static void printChangingTicketStatePage(Session session, SeatState state) throws IOException {
        // Выводим схему зала и возвращаем билет, соответствующий выборанному месту
        printSeats(session);
        String choice = "";
        do {
            System.out.print("Please enter a row number> ");
            String rowStr = reader.readLine();
            System.out.println();
            System.out.print("Please enter a column number> ");
            String colStr = reader.readLine();
            System.out.println();

            try {
                int row = Integer.parseInt(rowStr);
                int col = Integer.parseInt(colStr);
                if (session.getSeats()[row - 1][col - 1] == (state != SeatState.SOLD ? SeatState.SOLD : SeatState.FREE)) {
                    session.getSeats()[row - 1][col - 1] = state;
                    sessionDao.saveChanges();
                    System.out.println("The seat's state is changed successfully.");
                    System.out.println();
                    printMainMenu();
                    return;
                }

                System.out.println("Incorrect status changing.");
                System.out.println();

            } catch (IndexOutOfBoundsException e) {
                System.out.println("There is no seat with such coordinates.");
            } catch (NumberFormatException e) {
                System.out.println("It should be integer.");
            } catch (IOException e) {
                System.out.println("Error occurred while changing seat's status.");
            }

            System.out.print("Print q to return to main menu or something else to try again> ");
            choice = reader.readLine();
            System.out.println();
        } while (!Objects.equals(choice, "q"));

        printMainMenu();
    }

    private static void printEditingMoviesPage() throws IOException {
        // Интерфейс выбора способа редактирования списка фильмов
        while (true) {
            System.out.println("What you need to do?");
            System.out.println("Available options:");
            System.out.println("1 - Add movie");
            System.out.println("2 - Delete movie");
            System.out.println("3 - Return to main menu");
            System.out.println();
            System.out.print("You choice> ");

            String choice = reader.readLine();
            System.out.println();
            switch (choice) {
                case "1":
                    printMovieAddingPage();
                    break;
                case "2":
                    printDeletingMoviesPage();
                    break;
                case "3":
                    System.out.println("Returning to main menu...");
                    System.out.println();

                    printMainMenu();
                    break;
                default:
                    System.out.println("Please choose one of available options");
            }
        }
    }

    private static void printMovieAddingPage() throws IOException {
        // Запрашиваем у пользователя информацию о фильме и сохраняем ее
        String choice = "";
        do {
            System.out.print("Please enter a name of a movie> ");
            String name = reader.readLine();
            if (name.isEmpty()) continue;
            System.out.println();
            System.out.print("Please enter the director of the movie> ");
            String director = reader.readLine();
            if (director.isEmpty()) continue;
            System.out.println();
            System.out.print("Please enter the duration of the movie> ");
            String durationInput = reader.readLine();
            System.out.println();


            try {
                int duration = Integer.parseInt(durationInput);

                if (duration <= 0) {
                    System.out.println("Duration must be positive.");
                    System.out.println();
                    continue;
                }

                movieDao.save(new Movie(name, director, duration));
                System.out.println("The movie added successfully.");
                System.out.println();

                printMainMenu();
                break;

            } catch (IOException e) {
                System.out.println("Error occurred while saving movie.");
            } catch (IllegalArgumentException e) {
                System.out.println("Such film is already added.");
            }

            System.out.print("Print q to return to previous menu> ");
            choice = reader.readLine();
            System.out.println();
        } while (!Objects.equals(choice, "q"));
    }

    private static Session GetSessionFromUser() throws IOException {
        // Универскальный метод для выбора пользователем сеанса кинотеатра
        List<Session> sessions;
        try {
            sessions = sessionDao.getAll();
        } catch (IOException e) {
            System.out.println("Oops... Something with data went wrong.");
            System.out.println("Returning to main menu...");
            System.out.println();
            printMainMenu();
            return null;
        }

        if (sessions.isEmpty()) {
            System.out.println("System does not contain any information about sessions.");
            System.out.println("Returning to main menu...");
            System.out.println();
            printMainMenu();
            return null;
        }

        System.out.println("Sessions:");

        // Выводим все сеансы с номерами
        for (int i = 0; i < sessions.size(); ++i) {
            System.out.println((i + 1) + " " + sessions.get(i).toString());
        }

        while (true) {
            System.out.println();
            System.out.print("Enter number of session or q to return to main menu> ");

            String choice = reader.readLine();
            System.out.println();

            if (choice.equals("q")) {
                printMainMenu();
                break;
            }

            try {
                int index = Integer.parseInt(choice);
                return sessions.get(index - 1);
            } catch (IndexOutOfBoundsException e) {
                System.out.println("There is no film with such number.");
            } catch (NumberFormatException e) {
                System.out.println("It should be integer.");
            }
        }

        return null;
    }

    private static void printSeats(Session session) {
        // Вывод мест в зале на определенный сеанс в читаемом виде
        // punched - контроллер уже пробил билет, зритель зашел в зал
        System.out.println("S - sold, F - free, P - punched.");
        System.out.println("Seats on the chosen session:");
        System.out.println();

        AsciiTable table = new AsciiTable();
        table.addRule();

        SeatState[][] seats = session.getSeats();

        String[] firstRow = new String[seats[0].length + 1];
        firstRow[0] = "row\\col";
        for (int i = 0; i < seats[0].length; ++i) {
            firstRow[i + 1] = Integer.toString(i + 1);
        }
        table.addRow(firstRow);
        table.addRule();

        for (int i = 0; i < seats.length; ++i) {
            String[] row = new String[seats[i].length + 1];
            row[0] = Integer.toString(i + 1);

            for (int j = 0; j < seats[i].length; ++j) {
                row[j + 1] = seats[i][j] == SeatState.FREE ? "F" : seats[i][j] == SeatState.SOLD ? "S" : "P";
            }
            table.addRow(row);
            table.addRule();
        }
        System.out.println(table.render());
        System.out.println();
    }

    private static Movie getMovieByUser() throws IOException {
        // Выводим все фильмы и просим пользователя выбрать
        List<Movie> movies;
        try {
            movies = movieDao.getAll();
        } catch (IOException e) {
            System.out.println("Oops... Something with data went wrong.");
            System.out.println("Returning to main menu...");
            System.out.println();
            printMainMenu();
            return null;
        }

        if (movies.isEmpty()) {
            System.out.println("System does not contain any information about movies.");
            System.out.println("Returning to main menu...");
            System.out.println();
            printMainMenu();
            return null;
        }

        System.out.println("Movies:");

        for (int i = 0; i < movies.size(); ++i) {
            System.out.println((i + 1) + " " + movies.get(i).toString());
        }

        while (true) {
            System.out.println();
            System.out.print("Enter the number of movie you want to choose or q to return to main menu> ");

            String choice = reader.readLine();
            System.out.println();

            if (choice.equals("q")) {
                printMainMenu();
                break;
            }

            try {
                int index = Integer.parseInt(choice);
                return movies.get(index - 1);
            } catch (IndexOutOfBoundsException e) {
                System.out.println("There is no film with such number.");

            } catch (NumberFormatException e) {
                System.out.println("It should be integer.");
            }
        }
        return null;
    }

    private static void printDeletingMoviesPage() {
        // Удаляем выбранный пользователем фильм
        try {
            Movie movie = getMovieByUser();
            if (movie == null) {
                printMainMenu();
                return;
            }
            movieDao.delete(getMovieByUser());
            System.out.println("Movie deleted successfully");
            System.out.println();
        } catch (IOException e) {
            System.out.println("Error with data occurred.");
        }
    }
}
