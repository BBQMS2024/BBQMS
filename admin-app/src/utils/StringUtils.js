/**
 * @param classNames{} - an array of css classnames to concatenate. Even null/undefined are allowed (they will simply not be serialized into the final css class)
 * @returns {string} - resulting css class, conforming to css standard
 */
export function createClassName(classNames) {
    return classNames.reduce((prev, className) => {
        if (!className || className.trim() === 0) {
            return prev;
        }

        return prev + (prev.length > 0 ? ' ' : '') + className.trim()
    }, '');
}
