package com.mfrf.dawdletodo.exceptions;

public class AddTaskError extends Exception {
    public AddTaskError(String message) {
        super(message);
    }

    public static class GroupNotFoundError extends AddTaskError {

        public GroupNotFoundError(String id) {
            super(
                    "no group with specific id " + id + " found"
            );
        }
    }

    public static class AddTaskToAtomickContainerError extends AddTaskError {

        public AddTaskToAtomickContainerError() {
            super(
                    "cannot add group to Atomic task container "
            );
        }
    }
}

