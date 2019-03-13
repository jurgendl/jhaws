package org.jhaws.common.pool;

public enum JobState {
    DONE, ERROR, QUEUED, CANCELLED, EXECUTING;

    public static boolean accepts(JobState from, JobState to) {
        if (to == null) {
            return false;
        }
        if (from == null) {
            switch (to) {
                case QUEUED: {
                    return true;
                }
                case EXECUTING: {
                    return false;
                }
                case DONE: {
                    return false;
                }
                case ERROR: {
                    return false;
                }
                case CANCELLED: {
                    return true;
                }
            }
        }
        switch (from) {
            case QUEUED: {
                switch (to) {
                    case QUEUED: {
                        return false;
                    }
                    case EXECUTING: {
                        return true;
                    }
                    case DONE: {
                        return false;
                    }
                    case ERROR: {
                        return false;
                    }
                    case CANCELLED: {
                        return true;
                    }
                }
            }
            case EXECUTING: {
                switch (to) {
                    case QUEUED: {
                        return false;
                    }
                    case EXECUTING: {
                        return false;
                    }
                    case DONE: {
                        return true;
                    }
                    case ERROR: {
                        return true;
                    }
                    case CANCELLED: {
                        return true;
                    }
                }
            }
            case DONE: {
                switch (to) {
                    case QUEUED: {
                        return false;
                    }
                    case EXECUTING: {
                        return false;
                    }
                    case DONE: {
                        return false;
                    }
                    case ERROR: {
                        return false;
                    }
                    case CANCELLED: {
                        return false;
                    }
                }
            }
            case ERROR: {
                switch (to) {
                    case QUEUED: {
                        return false;
                    }
                    case EXECUTING: {
                        return false;
                    }
                    case DONE: {
                        return false;
                    }
                    case ERROR: {
                        return false;
                    }
                    case CANCELLED: {
                        return false;
                    }
                }
            }
            case CANCELLED: {
                switch (to) {
                    case QUEUED: {
                        return false;
                    }
                    case EXECUTING: {
                        return false;
                    }
                    case DONE: {
                        return false;
                    }
                    case ERROR: {
                        return false;
                    }
                    case CANCELLED: {
                        return false;
                    }
                }
            }
        }
        return false;
    }
}
