import { useSortContext } from '../../context/SortContext.jsx';
import { useSortSearchState } from '../../hooks/sortParamsHooks.jsx';

export function SortableHeader({ columnName, children }) {
    const { sort, updateSort } = useSortSearchState(columnName);
    const sortContext = useSortContext();

    function handleSort() {
        const newSort = updateSort();
        sortContext.onSort(newSort);
    }

    return (
        <th style={{ cursor: 'pointer' }}
            onClick={ handleSort }>
            { children }
        </th>
    )
}
