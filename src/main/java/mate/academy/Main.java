package mate.academy;

import java.time.LocalDate;
import java.time.LocalDateTime;
import mate.academy.lib.Injector;
import mate.academy.model.CinemaHall;
import mate.academy.model.Movie;
import mate.academy.model.MovieSession;
import mate.academy.model.User;
import mate.academy.security.AuthenticationService;
import mate.academy.service.CinemaHallService;
import mate.academy.service.MovieService;
import mate.academy.service.MovieSessionService;
import mate.academy.service.OrderService;
import mate.academy.service.ShoppingCartService;
import mate.academy.service.UserService;

public class Main {
    private static final Injector injector = Injector.getInstance("mate.academy");
    private static AuthenticationService authenticationService =
            (AuthenticationService) injector.getInstance(AuthenticationService.class);
    private static MovieService movieService =
            (MovieService) injector.getInstance(MovieService.class);
    private static CinemaHallService cinemaHallService =
            (CinemaHallService) injector.getInstance(CinemaHallService.class);
    private static MovieSessionService movieSessionService =
            (MovieSessionService) injector.getInstance(MovieSessionService.class);
    private static UserService userService =
            (UserService) injector.getInstance(UserService.class);
    private static OrderService orderService =
            (OrderService) injector.getInstance(OrderService.class);
    private static ShoppingCartService shoppingCartService =
            (ShoppingCartService) injector.getInstance(ShoppingCartService.class);

    public static void main(String[] args) {
        Movie fastAndFurious = new Movie("Fast and Furious");
        fastAndFurious.setDescription("An action film about street racing, heists, and spies.");
        movieService.add(fastAndFurious);
        System.out.println(movieService.get(fastAndFurious.getId()));
        movieService.getAll().forEach(System.out::println);

        CinemaHall firstCinemaHall = new CinemaHall();
        firstCinemaHall.setCapacity(100);
        firstCinemaHall.setDescription("first hall with capacity 100");

        CinemaHall secondCinemaHall = new CinemaHall();
        secondCinemaHall.setCapacity(200);
        secondCinemaHall.setDescription("second hall with capacity 200");

        cinemaHallService.add(firstCinemaHall);
        cinemaHallService.add(secondCinemaHall);

        System.out.println(cinemaHallService.getAll());
        System.out.println(cinemaHallService.get(firstCinemaHall.getId()));

        MovieSession tomorrowMovieSession = new MovieSession();
        tomorrowMovieSession.setCinemaHall(firstCinemaHall);
        tomorrowMovieSession.setMovie(fastAndFurious);
        tomorrowMovieSession.setShowTime(LocalDateTime.now().plusDays(1L));

        MovieSession yesterdayMovieSession = new MovieSession();
        yesterdayMovieSession.setCinemaHall(firstCinemaHall);
        yesterdayMovieSession.setMovie(fastAndFurious);
        yesterdayMovieSession.setShowTime(LocalDateTime.now().minusDays(1L));

        movieSessionService.add(tomorrowMovieSession);
        movieSessionService.add(yesterdayMovieSession);

        System.out.println(movieSessionService.get(yesterdayMovieSession.getId()));
        System.out.println(movieSessionService.findAvailableSessions(
                        fastAndFurious.getId(), LocalDate.now()));

        System.out.println("----------User Register----------");
        User bob = authenticationService.register("olk7865@gmail.com", "45879kf5d4");
        User alice = authenticationService.register("ifhbd78.dd@gmail.com", "7dfd5v5d");
        System.out.println(bob);
        System.out.println(alice);
        System.out.println("----------------------------------");

        System.out.println("Tickets buy...");
        shoppingCartService.addSession(tomorrowMovieSession, bob);
        shoppingCartService.addSession(yesterdayMovieSession, alice);
        shoppingCartService.addSession(yesterdayMovieSession, bob);

        System.out.println("----------Get shopping card by user----------");
        System.out.println(shoppingCartService.getByUser(bob));
        System.out.println(shoppingCartService.getByUser(alice));
        System.out.println("---------------------------------------------");

        System.out.println("Completing orders...");
        orderService.completeOrder(shoppingCartService.getByUser(bob));
        orderService.completeOrder(shoppingCartService.getByUser(alice));

        System.out.println("----------Get order history----------");
        System.out.println(orderService.getOrdersHistory(bob));
        System.out.println(orderService.getOrdersHistory(alice));
        System.out.println("-------------------------------------");
    }
}
