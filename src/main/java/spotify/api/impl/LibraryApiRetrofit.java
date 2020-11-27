package spotify.api.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import retrofit2.Call;
import retrofit2.Response;
import spotify.api.interfaces.LibraryApi;
import spotify.exceptions.HttpRequestFailedException;
import spotify.factories.RetrofitHttpServiceFactory;
import spotify.models.albums.SavedAlbumFull;
import spotify.models.paging.Paging;
import spotify.retrofit.services.LibraryService;
import spotify.utils.LoggingUtil;
import spotify.utils.ResponseChecker;
import spotify.utils.ValidatorUtil;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public class LibraryApiRetrofit implements LibraryApi {
    private final Logger logger = LoggerFactory.getLogger(LibraryApiRetrofit.class);
    private final String accessToken;
    private final LibraryService libraryService;

    public LibraryApiRetrofit(final String accessToken) {
        this.accessToken = accessToken;
        this.libraryService = RetrofitHttpServiceFactory.getLibraryService();
    }

    @Override
    public List<Boolean> hasSavedAlbums(List<String> listOfAlbumIds) {
        String albumIds = String.join(",", listOfAlbumIds);

        logger.trace("Constructing HTTP call check if current user has saved the given albums ");
        Call<List<Boolean>> httpCall = libraryService.hasSavedAlbums("Bearer " + this.accessToken, albumIds);

        try {
            logger.info("Executing HTTP call to check saved albums.");
            logger.debug(String.format("Checking albums with following album ids: %s.", albumIds));
            LoggingUtil.logHttpCall(logger, httpCall);
            Response<List<Boolean>> response = httpCall.execute();

            ResponseChecker.throwIfRequestHasNotBeenFulfilledCorrectly(response.errorBody());

            logger.info("Saved albums have been successfully checked");
            return response.body();
        } catch (IOException ex) {
            logger.error("HTTP request to check saved albums has failed.");
            throw new HttpRequestFailedException(ex.getMessage());
        }
    }

    @Override
    public List<Boolean> hasSavedShows(List<String> listOfShowIds) {
        String showIds = String.join(",", listOfShowIds);

        logger.trace("Constructing HTTP call check if current user has saved the given shows ");
        Call<List<Boolean>> httpCall = libraryService.hasSavedShows("Bearer " + this.accessToken, showIds);

        try {
            logger.info("Executing HTTP call to check saved shows.");
            logger.debug(String.format("Checking shows with following show ids: %s.", showIds));
            LoggingUtil.logHttpCall(logger, httpCall);
            Response<List<Boolean>> response = httpCall.execute();

            ResponseChecker.throwIfRequestHasNotBeenFulfilledCorrectly(response.errorBody());

            logger.info("Saved shows have been successfully checked");
            return response.body();
        } catch (IOException ex) {
            logger.error("HTTP request to check saved shows has failed.");
            throw new HttpRequestFailedException(ex.getMessage());
        }
    }

    @Override
    public List<Boolean> hasSavedTracks(List<String> listOfTrackIds) {
        String trackIds = String.join(",", listOfTrackIds);

        logger.trace("Constructing HTTP call check if current user has saved the given tracks ");
        Call<List<Boolean>> httpCall = libraryService.hasSavedTracks("Bearer " + this.accessToken, trackIds);

        try {
            logger.info("Executing HTTP call to check saved tracks.");
            logger.debug(String.format("Checking tracks with following track ids: %s.", trackIds));
            LoggingUtil.logHttpCall(logger, httpCall);
            Response<List<Boolean>> response = httpCall.execute();

            ResponseChecker.throwIfRequestHasNotBeenFulfilledCorrectly(response.errorBody());

            logger.info("Saved tracks have been successfully checked");
            return response.body();
        } catch (IOException ex) {
            logger.error("HTTP request to check saved tracks has failed.");
            throw new HttpRequestFailedException(ex.getMessage());
        }
    }

    @Override
    public Paging<SavedAlbumFull> getSavedAlbums(Map<String, String> options) {
        options = ValidatorUtil.optionsValueCheck(options);
        logger.trace("Constructing HTTP call fetch current user saved albums");
        Call<Paging<SavedAlbumFull>> httpCall = libraryService.getSavedAlbums("Bearer " + this.accessToken, options);

        try {
            logger.info("Executing HTTP call to fetch current user saved albums");
            logger.debug(String.format("Fetching current user saved albums with the following values: %s.", options));
            LoggingUtil.logHttpCall(logger, httpCall);
            Response<Paging<SavedAlbumFull>> response = httpCall.execute();

            ResponseChecker.throwIfRequestHasNotBeenFulfilledCorrectly(response.errorBody());

            logger.info("Saved albums have been successfully fetched");
            return response.body();
        } catch (IOException ex) {
            logger.error("HTTP request to fetch saved albums has failed.");
            throw new HttpRequestFailedException(ex.getMessage());
        }
    }
}