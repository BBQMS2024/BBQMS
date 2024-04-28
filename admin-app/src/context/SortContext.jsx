import { createContext, useContext } from 'react';

const SortContext = createContext({ onSort: () => {} });

export function useSortContext() {
    return useContext(SortContext);
}

export function SortContextProvider({ onSort, children }) {
    return (
        <SortContext.Provider value={{ onSort }}>
            { children }
        </SortContext.Provider>
    )
}
