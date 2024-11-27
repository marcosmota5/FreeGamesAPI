package com.app.freegamesapi.Models;

import com.app.freegamesapi.CustomExceptions.ApiCallFailedException;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import com.google.gson.*;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class Game {

    // Fields
    private int id;
    private String title;
    private String thumbnail;
    private String status;
    private String shortDescription;
    private String description;
    private String gameUrl;
    private String genre;
    private String platform;
    private String publisher;
    private String developer;
    private LocalDate releaseDate;
    private String freeToGameProfileUrl;
    List<SystemRequirement> systemRequirements;
    List<String> screenshots;

    // Constructors
    public Game(int id, String title, String thumbnail, String status, String shortDescription,
                String description, String gameUrl, String genre, String platform,
                String publisher, String developer, LocalDate releaseDate,
                String freeToGameProfileUrl, List<SystemRequirement> systemRequirements,
                List<String> screenshots) {
        this.id = id;
        this.title = title;
        this.thumbnail = thumbnail;
        this.status = status;
        this.shortDescription = shortDescription;
        this.description = description;
        this.gameUrl = gameUrl;
        this.genre = genre;
        this.platform = platform;
        this.publisher = publisher;
        this.developer = developer;
        this.releaseDate = releaseDate;
        this.freeToGameProfileUrl = freeToGameProfileUrl;
        this.systemRequirements = systemRequirements;
        this.screenshots = screenshots;
    }

    public Game(int id, String title, String thumbnail, String shortDescription,
                String gameUrl, String genre, String platform,
                String publisher, String developer, LocalDate releaseDate,
                String freeToGameProfileUrl) {
        this.id = id;
        this.title = title;
        this.thumbnail = thumbnail;
        this.shortDescription = shortDescription;
        this.gameUrl = gameUrl;
        this.genre = genre;
        this.platform = platform;
        this.publisher = publisher;
        this.developer = developer;
        this.releaseDate = releaseDate;
        this.freeToGameProfileUrl = freeToGameProfileUrl;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getShortDescription() {
        return shortDescription;
    }

    public void setShortDescription(String shortDescription) {
        this.shortDescription = shortDescription;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getGameUrl() {
        return gameUrl;
    }

    public void setGameUrl(String gameUrl) {
        this.gameUrl = gameUrl;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public String getDeveloper() {
        return developer;
    }

    public void setDeveloper(String developer) {
        this.developer = developer;
    }

    public LocalDate getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(LocalDate releaseDate) {
        this.releaseDate = releaseDate;
    }

    public String getFreeToGameProfileUrl() {
        return freeToGameProfileUrl;
    }

    public void setFreeToGameProfileUrl(String freeToGameProfileUrl) {
        this.freeToGameProfileUrl = freeToGameProfileUrl;
    }

    public List<SystemRequirement> getSystemRequirements() {
        return systemRequirements;
    }

    public void setSystemRequirements(List<SystemRequirement> systemRequirements) {
        this.systemRequirements = systemRequirements;
    }

    public List<String> getScreenshots() {
        return screenshots;
    }

    public void setScreenshots(List<String> screenshots) {
        this.screenshots = screenshots;
    }

    // Methods
    public static CompletableFuture<Game> getGameById(int id) throws ApiCallFailedException {
        // Create an HttpClient object
        HttpClient client = HttpClient.newHttpClient();

        // Create the request URL
        String requestUrl = String.format("https://www.freetogame.com/api/game?id=%d", id);

        // Create a GET request
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(requestUrl))
                .build();

        // Send the request asynchronously
        return client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(HttpResponse::body)
                .thenApply(response -> {
                    try {
                        // Parse the JSON response
                        JsonObject gameObject = JsonParser.parseString(response).getAsJsonObject();

                        // Extract system requirements
                        JsonObject systemRequirementsJson = gameObject.has("minimum_system_requirements")
                                && !gameObject.get("minimum_system_requirements").isJsonNull()
                                ? gameObject.getAsJsonObject("minimum_system_requirements")
                                : null;

                        // Create the system requirement object and set the properties if they have value
                        SystemRequirement systemRequirement = new SystemRequirement(
                                systemRequirementsJson != null && systemRequirementsJson.has("os") ? systemRequirementsJson.get("os").getAsString() : "",
                                systemRequirementsJson != null && systemRequirementsJson.has("processor") ? systemRequirementsJson.get("processor").getAsString() : "",
                                systemRequirementsJson != null && systemRequirementsJson.has("memory") ? systemRequirementsJson.get("memory").getAsString() : "",
                                systemRequirementsJson != null && systemRequirementsJson.has("graphics") ? systemRequirementsJson.get("graphics").getAsString() : "",
                                systemRequirementsJson != null && systemRequirementsJson.has("storage") ? systemRequirementsJson.get("storage").getAsString() : ""
                        );

                        // Extract screenshots if there are any and depending on the amount (because it can vary)
                        List<String> screenshots = new ArrayList<>();
                        if (gameObject.has("screenshots") && !gameObject.get("screenshots").isJsonNull()) {
                            JsonArray screenshotArray = gameObject.getAsJsonArray("screenshots");
                            for (JsonElement screenshotElement : screenshotArray) {
                                JsonObject screenshotObject = screenshotElement.getAsJsonObject();
                                if (screenshotObject.has("image")) {
                                    screenshots.add(screenshotObject.get("image").getAsString());
                                }
                            }
                        }

                        // Create and return the Game object
                        return new Game(
                                gameObject.get("id").getAsInt(),
                                gameObject.get("title").getAsString(),
                                gameObject.get("thumbnail").getAsString(),
                                gameObject.get("status").getAsString(),
                                gameObject.get("short_description").getAsString(),
                                gameObject.get("description").getAsString(),
                                gameObject.get("game_url").getAsString(),
                                gameObject.get("genre").getAsString(),
                                gameObject.get("platform").getAsString(),
                                gameObject.get("publisher").getAsString(),
                                gameObject.get("developer").getAsString(),
                                LocalDate.parse(gameObject.get("release_date").getAsString().replace("00", "01")),
                                gameObject.get("freetogame_profile_url").getAsString(),
                                List.of(systemRequirement),
                                screenshots
                        );
                    } catch (Exception e) {
                        e.printStackTrace();
                        throw new ApiCallFailedException("Failed to parse Game object", e);
                    }
                });
    }

    public static CompletableFuture<List<Game>> getGameList(List<String> categories, String platform, String sortBy, String searchTitle) throws ApiCallFailedException {
        // Create an HttpClient object
        HttpClient client = HttpClient.newHttpClient();

        // Build the base URL
        String baseUrl;

        if (categories == null || categories.isEmpty()) {
            baseUrl = "https://www.freetogame.com/api/games";
        } else {
            String tags = categories.stream()
                    .map(category -> category.toLowerCase().replace(" ", "-"))
                    .reduce((tag1, tag2) -> tag1 + "." + tag2)
                    .orElse("");
            baseUrl = "https://www.freetogame.com/api/filter?tag=" + tags;
        }

        // Append platform and sortBy parameters if they are not empty
        if (!platform.isEmpty()) {
            // If the base url is the base one, add a question mark for the platform, otherwise add a &
            if (baseUrl.endsWith("/api/games")) {
                baseUrl += "?";
            }
            else {
                baseUrl += "&";
            }

            // Concatenate the platform
            baseUrl += "platform=" + platform.toLowerCase().replace(" ", "-");
        }
        if (!sortBy.isEmpty()) {
            // If the base url is the base one, add a question mark for the platform, otherwise add a &
            if (baseUrl.endsWith("/api/games")) {
                baseUrl += "?";
            }
            else {
                baseUrl += "&";
            }

            // Concatenate the sort by
            baseUrl += "&sort-by=" + sortBy.toLowerCase().replace(" ", "-");
        }

        // Create the GET request
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(baseUrl))
                .build();

        // Send the request asynchronously
        return client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(HttpResponse::body)
                .thenApply(response -> {
                    try {
                        // Parse the JSON response
                        JsonArray gameArray = JsonParser.parseString(response).getAsJsonArray();

                        // Extract the list of Game objects
                        List<Game> games = new ArrayList<>();
                        for (JsonElement element : gameArray) {
                            // Get a game object form the json
                            JsonObject gameObject = element.getAsJsonObject();

                            // Create a game variable
                            Game game = new Game(
                                    gameObject.get("id").getAsInt(),
                                    gameObject.get("title").getAsString(),
                                    gameObject.get("thumbnail").getAsString(),
                                    gameObject.get("short_description").getAsString(),
                                    gameObject.get("game_url").getAsString(),
                                    gameObject.get("genre").getAsString(),
                                    gameObject.get("platform").getAsString(),
                                    gameObject.get("publisher").getAsString(),
                                    gameObject.get("developer").getAsString(),
                                    LocalDate.parse(gameObject.get("release_date").getAsString().replace("00", "01")),
                                    gameObject.get("freetogame_profile_url").getAsString()
                            );

                            // Add the game to the games list
                            games.add(game);
                        }

                        // Filter games by searchTitle (case-insensitive)
                        if (searchTitle != null && !searchTitle.trim().isEmpty()) {
                            String trimmedSearchTitle = searchTitle.trim(); // Remove extra spaces
                            games = games.stream()
                                    .filter(game -> game.getTitle().toLowerCase().contains(trimmedSearchTitle.toLowerCase()))
                                    .toList();
                        }

                        // Return the games list
                        return games;
                    } catch (Exception e) {
                        e.printStackTrace();
                        throw new ApiCallFailedException("Failed to parse game list", e);
                    }
                });
    }

    @Override
    public String toString() {
        // Display the title information in the ListView
        return title;
    }
}