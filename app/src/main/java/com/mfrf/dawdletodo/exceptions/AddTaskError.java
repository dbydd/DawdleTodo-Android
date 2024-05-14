package com.mfrf.dawdletodo.exceptions;

public class AddTaskError extends Exception {
    private AddTaskError(String message) {
        super(message);
    }

    public static class GroupNotFoundError extends AddTaskError {

        public GroupNotFoundError(String id) {
            super(
                    "no group with specific id " + id + " found"
            );
        }
    }

    public static class CannotAddToSingleTaskContainerError extends AddTaskError {

        public CannotAddToSingleTaskContainerError() {
            super(
                    "cannot add group to single task container "
            );
        }
    }
}

