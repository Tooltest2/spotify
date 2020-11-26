package spotify.api.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import retrofit2.Call;
import retrofit2.Response;
import spotify.api.enums.EntityType;
import spotify.api.interfaces.FollowApi;
import spotify.exceptions.HttpRequestFailedException;
import spotify.exceptions.SpotifyActionFailedException;
import spotify.factories.RetrofitHttpServiceFactory;
import spotify.models.artists.ArtistFullCursorBasedPaging;
import spotify.models.artists.ArtistFullCursorBasedPagingWrapper;
import spotify.models.playlists.FollowPlaylistRequestBody;
import spotify.retrofit.services.FollowService;
import spotify.utils.LoggingUtil;
import spotify.utils.ResponseChecker;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public class FollowApiRetrofit implements FollowApi {
    private final Logger logger = LoggerFactory.getLogger(EpisodeApiRetrofit.class);
    private final String accessToken;
    private final FollowService followService;

    public FollowApiRetrofit(final String accessToken) {
        this.accessToken = accessToken;
        this.followService = RetrofitHttpServiceFactory.getFollowService();
    }

    @Override
    public List<Boolean> isFollowing(EntityType entityType, List<String> listOfEntityIds) {
        String entityIds = String.join(",", listOfEntityIds);

        logger.trace("Constructing HTTP call to check if user follows the provided entities.");
        Call<List<Boolean>> httpCall = followService.isFollowing("Bearer " + this.accessToken, entityType, entityIds);

        try {
            logger.info("Executing HTTP call to check if user follows the provided entities.");
            logger.debug(String.format("Fetching %s following list with following entity ids: %s.", entityType, entityIds));
            LoggingUtil.logHttpCall(logger, httpCall);
            Response<List<Boolean>> response = httpCall.execute();

            ResponseChecker.throwIfRequestHasNotBeenFulfilledCorrectly(response.errorBody());

            logger.info("Following list has been successfully fetched.");
            return response.body();
        } catch (IOException ex) {
            logger.error("HTTP request to fetch following list has failed.");
            throw new HttpRequestFailedException(ex.getMessage());
        }
    }

    @Override
    public List<Boolean> isFollowingPlaylist(String playlistId, List<String> listOfUserIds) {
        String userIds = String.join(",", listOfUserIds);

        logger.trace("Constructing HTTP call to check if users are following the playlist.");
        Call<List<Boolean>> httpCall = followService.isFollowingPlaylist("Bearer " + this.accessToken, playlistId, userIds);

        try {
            logger.info("Executing HTTP call to check if users are following the playlist.");
            logger.debug(String.format("Fetching %s playlist following list with following user ids: %s.", playlistId, userIds));
            LoggingUtil.logHttpCall(logger, httpCall);
            Response<List<Boolean>> response = httpCall.execute();

            ResponseChecker.throwIfRequestHasNotBeenFulfilledCorrectly(response.errorBody());

            logger.info("Following list has been successfully fetched.");
            return response.body();
        } catch (IOException ex) {
            logger.error("HTTP request to fetch following list has failed.");
            throw new HttpRequestFailedException(ex.getMessage());
        }
    }

    @Override
    public void followEntities(EntityType entityType, List<String> listOfEntityIds) {
        String entityIds = String.join(",", listOfEntityIds);

        logger.trace("Constructing HTTP call to follow entities.");
        Call<Void> httpCall = followService.followEntities("Bearer " + this.accessToken, entityType, entityIds);

        try {
            logger.info("Executing HTTP call to follow entities.");
            logger.debug(String.format("Following entities with following entity ids: %s.", entityIds));
            LoggingUtil.logHttpCall(logger, httpCall);
            Response<Void> response = httpCall.execute();

            ResponseChecker.throwIfRequestHasNotBeenFulfilledCorrectly(response.errorBody());

            logger.info("Entities have been successfully followed.");
        } catch (IOException ex) {
            logger.error("HTTP request to follow entities has failed.");
            throw new HttpRequestFailedException(ex.getMessage());
        }
    }

    @Override
    public void followPlaylist(String playlistId, boolean setPlaylistPublic) {
        logger.trace("Constructing HTTP call to follow playlist.");
        Call<Void> httpCall = followService.followPlaylist("Bearer " + this.accessToken, playlistId, new FollowPlaylistRequestBody(setPlaylistPublic));

        try {
            logger.info("Executing HTTP call to follow playlist.");
            logger.debug(String.format("Following playlist %s and set it to %s", playlistId, setPlaylistPublic ? "public" : "private"));
            LoggingUtil.logHttpCall(logger, httpCall);
            Response<Void> response = httpCall.execute();

            ResponseChecker.throwIfRequestHasNotBeenFulfilledCorrectly(response.errorBody());

            logger.info("Playlist has been successfully followed.");
        } catch (IOException ex) {
            logger.error("HTTP request to follow playlist has failed.");
            throw new HttpRequestFailedException(ex.getMessage());
        }
    }

    @Override
    public ArtistFullCursorBasedPaging getFollowedArtists(EntityType entityType, Map<String, String> options) {
        logger.trace("Constructing HTTP call to fetch followed artists of the current user.");
        Call<ArtistFullCursorBasedPagingWrapper> httpCall = followService.getFollowedArtists("Bearer " + this.accessToken, entityType, options);

        try {
            logger.info("Executing HTTP call to fetch followed artists of the current user.");
            logger.debug(String.format("Fetching current user's followed artists with the following values: %s", options));
            LoggingUtil.logHttpCall(logger, httpCall);
            Response<ArtistFullCursorBasedPagingWrapper> response = httpCall.execute();

            ResponseChecker.throwIfRequestHasNotBeenFulfilledCorrectly(response.errorBody());

            if (response.body() != null) {
                logger.info("Followed artists has been successfully followed.");
                return response.body().getArtists();
            }

            final String errorMessage = "Empty response body has been returned. Reason is unknown";

            logger.error(errorMessage);
            throw new SpotifyActionFailedException(errorMessage);
        } catch (IOException ex) {
            logger.error("HTTP request to fetch followed artists has failed.");
            throw new HttpRequestFailedException(ex.getMessage());
        }
    }

    @Override
    public void unfollowEntities(EntityType entityType, List<String> listOfEntityIds) {
        String entityIds = String.join(",", listOfEntityIds);

        logger.trace("Constructing HTTP call to unfollow entities.");
        Call<Void> httpCall = followService.unfollowEntities("Bearer " + this.accessToken, entityType, entityIds);

        try {
            logger.info("Executing HTTP call to unfollow entities.");
            logger.debug(String.format("Unfollowing entities with following entity ids: %s.", entityIds));
            LoggingUtil.logHttpCall(logger, httpCall);
            Response<Void> response = httpCall.execute();

            ResponseChecker.throwIfRequestHasNotBeenFulfilledCorrectly(response.errorBody());

            logger.info("Entities have been successfully unfollowed.");
        } catch (IOException ex) {
            logger.error("HTTP request to unfollow entities has failed.");
            throw new HttpRequestFailedException(ex.getMessage());
        }
    }

    @Override
    public void unfollowPlaylist(String playlistId) {
        logger.trace("Constructing HTTP call to unfollow playlist.");
        Call<Void> httpCall = followService.unfollowPlaylist("Bearer " + this.accessToken, playlistId);

        try {
            logger.info("Executing HTTP call to unfollow playlist.");
            logger.debug(String.format("Unfollowing playlist %s ", playlistId));
            LoggingUtil.logHttpCall(logger, httpCall);
            Response<Void> response = httpCall.execute();

            ResponseChecker.throwIfRequestHasNotBeenFulfilledCorrectly(response.errorBody());

            logger.info("Playlist has been successfully unfollowed.");
        } catch (IOException ex) {
            logger.error("HTTP request to unfollow playlist has failed.");
            throw new HttpRequestFailedException(ex.getMessage());
        }
    }
}