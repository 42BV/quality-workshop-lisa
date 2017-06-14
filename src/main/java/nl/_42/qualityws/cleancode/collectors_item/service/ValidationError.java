package nl._42.qualityws.cleancode.collectors_item.service;

class ValidationError {

    private final String description;

    public ValidationError(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

}
