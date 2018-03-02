package com.gibson.analytics.init;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;

/**
 * The Class CsvSource.
 */
public class CsvSource {

	final static Logger logger = LoggerFactory.getLogger(CsvSource.class);

	private static final String SEPERATOR = ",";

	Optional<List<String>> header =  Optional.empty();
	Optional<List<List<String>>> records = Optional.empty();

	/**
	 * Checks if is initialized.
	 *
	 * @return true, if is initialized
	 */
	public boolean isInitialized(){
		return header.isPresent() && records.isPresent();
	}

	/**
	 * Gets the header.
	 *
	 * @return the header
	 */
	public Optional<List<String>> getHeader(){
		return header;
	}

	/**
	 * Gets the records.
	 *
	 * @return the records
	 */
	public Optional<List<List<String>>> getRecords() {
		return records;
	}

	/**
	 * Load from.
	 *
	 * @param resource the resource
	 */
	protected void loadFrom(Resource resource) {
		try {
			Path path = resource.getFile().toPath();
			loadFrom(path);
		} catch (IOException e) {
			logger.error(e.getMessage());
		}
	}

	/**
	 * Read records.
	 *
	 * @param path the path
	 * @return the optional
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	private Optional<List<List<String>>> readRecords(Path path) throws IOException {
		List<List<String>> r = 
				Files.lines(path).skip(1).map(l -> Arrays.asList(l.split(SEPERATOR))).collect(Collectors.toList());

		return Optional.of(r);
	}

	/**
	 * Read header.
	 *
	 * @param p the p
	 * @return the optional
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	private Optional<List<String>> readHeader(Path p) throws IOException {
		return Files.lines(p).findFirst().map(l -> Arrays.asList(l.split(SEPERATOR)));
	}

	/**
	 * Load from.
	 *
	 * @param path the path
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	protected void loadFrom(Path path) throws IOException {		
		header = readHeader(path);
		records = readRecords(path);
	}
}
