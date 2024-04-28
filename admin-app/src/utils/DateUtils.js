function parseDate(date) {
    return new Date(Date.parse(date))
}

function format(date, options) {
    return date && date.toLocaleString('en-GB', { ...options })
}

function getDate(date) {
    return date instanceof Date ? date : parseDate(date);
}

/**
 * @param date - either a string, or a Date object to be formatted
 * @returns {*} - a string representing the date in the format: 14 Apr 2023, 13:13 (for example)
 */
export function formatDate(date) {
    return format(getDate(date), {
        month: 'short',
        day: 'numeric',
        year: 'numeric',
        hour: 'numeric',
        minute: '2-digit'
    });
}

/**
 * Extracts the time from a date
 * @param date - either a string, or a Date object to be formatted
 * @returns {*} - a string representation of the date in the format: 19:31 (for example)
 */
export function formatTime(date) {
    return format(getDate(date), {
        hour: 'numeric',
        minute: '2-digit'
    })
}
