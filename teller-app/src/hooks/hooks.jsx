import { useEffect } from 'react';

/**
 * A hook for using the native JS interval API. The interval is released after the user component dismounts.
 * @param callback function to be executed
 * @param period interval between executions of the callback function. The first execution happens instantly.
 */
export function useInterval(callback, period) {
    useEffect(() => {
        const interval = setInterval(() => callback(), period)
        return () => clearInterval(interval);
    }, [ period ]);
}
