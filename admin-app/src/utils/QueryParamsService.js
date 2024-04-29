/**
 * Used for appending simple (of primitive types - string, int etc.) query params to url string
 * @param name{string} - name of the query parameter
 * @param value{string} - value of the query parameter
 * @param url{string} - url to which we are appending the query parameter
 * @returns {string} - url with the query parameter appended
 */
export function addSimpleParamToUrl(name, value, url) {
    if (url.includes('?')) {
        // There are already url parameters present - we should add ours with '&'
        return `${url}&${name}=${value}`;
    } else {
        // There are no params before ours, we can add it with '?'
        return `${url}?${name}=${value}`;
    }
}

/**
 * @param params - an array of objects in format { name: paramName, value: paramValue }. For example [{ name: 'sort', value: 'id' }]
 * @param url{string} - url to which we are appending the query parameters
 * @returns {string} - url with the query parameter appended
 */
export function addArrayToUrl(params, url) {
    return params.reduce((prev, item, index) => {
        if (index === 0) {
            return `${prev}?${item.name}=${item.value}`;
        } else {
            return `${prev}&${item.name}=${item.value}`;
        }
    }, url);
}

/**
 * @param sort{string} - sort object represented as a string in format name,direction, for example id,asc
 * @param url{string} - url to which we are appending the sort parameter
 * @returns {string} - url with the sort param appended
 */
export function addSortToUrl(sort, url) {
    return addSimpleParamToUrl('sort', sort, url);
}

export function parseSortFromUrl(url) {
    const searchParams = url.split('?')[1];

    if (searchParams) {
        const sortParam = searchParams.split('&').find(param => param.split('=')[0] === 'sort');
        return decodeURIComponent(sortParam.split('=')[1]);
    }
}
