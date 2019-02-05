package hello;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

@Component
class AppRunner implements CommandLineRunner {

	private final static Logger logger = LoggerFactory.getLogger(AppRunner.class);

	private final BookingRepository bookingsRepository;

	public AppRunner(BookingRepository bookingsRepository) {
		this.bookingsRepository = bookingsRepository;
	}

	@Override
	@Transactional
	public void run(String... args) throws Exception {
		bookingsRepository.book((long) 1, "Alice");
		bookingsRepository.book((long) 2, "Bob");
		bookingsRepository.book((long) 3, "Carol");
		Assert.isTrue(bookingsRepository.count() == 3, "First booking should work with no problem");
		logger.info("Alice, Bob and Carol have been booked");
		try {
			bookingsRepository.book((long) 4, "Chris");
			bookingsRepository.book((long) 5, "Samuel");
		} catch (RuntimeException e) {
			logger.info("v--- The following exception is expect because 'Samuel' is too " + "big for the DB ---v");
			logger.error(e.getMessage());
		}

		for (Booking booking : bookingsRepository.findAll()) {
			logger.info("So far, " + booking.getFirstName() + " is booked.");
		}
		logger.info("You shouldn't see Chris or Samuel. Samuel violated DB constraints, "
				+ "and Chris was rolled back in the same TX");
		Assert.isTrue(bookingsRepository.findAll().size() == 3, "'Samuel' should have triggered a rollback");

		try {
			bookingsRepository.book((long) 6, "Buddy");
			bookingsRepository.book((long) 7, null);
		} catch (RuntimeException e) {
			logger.info("v--- The following exception is expect because null is not " + "valid for the DB ---v");
			logger.error(e.getMessage());
		}

		for (Booking booking : bookingsRepository.findAll()) {
			logger.info("So far, " + booking.getFirstName() + " is booked.");
		}

		logger.info("You shouldn't see Buddy or null. null violated DB constraints, and "
				+ "Buddy was rolled back in the same TX");
		Assert.isTrue(bookingsRepository.findAll().size() == 3, "'null' should have triggered a rollback");

		System.out.println("done");
	}

}