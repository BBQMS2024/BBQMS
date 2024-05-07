import { useSearchParams } from 'react-router-dom';

export function useSortSearchState(column) {
    const [ search, setSearch ] = useSearchParams();

    function serializeToUrl(column, direction) {
        return `${column},${direction}`;
    }

    function deserializeFromUrl(sortQueryParam) {
        if (sortQueryParam) {
            const sortParts = sortQueryParam.split(',')
            return {
                column: sortParts[0],
                direction: sortParts[1]
            }
        }
    }

    function searchContainsColumn(column) {
        const sortParam = getCurrentSort();

        if (sortParam) {
            return deserializeFromUrl(sortParam).column === column;
        }

        return false;
    }

    function changeSortDirection() {
        const sortParam = getCurrentSort();

        if (sortParam) {
            const { column, direction } = deserializeFromUrl(sortParam);

            if (direction === 'asc') {
                setSortDirection(column, 'desc');
            } else if (direction === 'desc') {
                removeSort()
            } else {
                setSortDirection(column, 'asc');
            }
        }
    }

    function setSortDirection(column, direction = 'asc') {
        search.set('sort', serializeToUrl(column, direction));
        setSearch(prev => search);
    }

    function getCurrentSort() {
        return search.get('sort');
    }

    function removeSort() {
        search.delete('sort');
        setSearch(prev => search)
    }

    function updateSort(column) {
        if (searchContainsColumn(column)) {
            changeSortDirection();
        } else {
            setSortDirection(column, 'asc');
        }
        return getCurrentSort();
    }

    return { sort: getCurrentSort(), updateSort: () => updateSort(column) };
}
