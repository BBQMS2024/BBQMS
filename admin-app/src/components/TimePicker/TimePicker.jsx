import { useState } from 'react';
import { createClassName } from '../../utils/StringUtils.js'

export function TimePicker({ title, onChange, className }) {
    const [ hours, setHours ] = useState('');
    const [ minutes, setMinutes ] = useState('');

    function handleChange(hours, minutes) {
        onChange({
            hour: hours && hours !== '' ? parseInt(hours) : 0,
            minutes: minutes && minutes !== '' ? parseInt(minutes) : 0
        });
    }

    function handleHourChange(value) {
        setHours(value);
        handleChange(value, minutes);
    }

    function handleMinuteChange(value) {
        setMinutes(value);
        handleChange(hours, value);
    }

    return (
        <div className={ createClassName([ 'd-inline-flex flex-column gap-1', className ]) }>
            <div>{ title }</div>
            <div className="d-inline-flex gap-2 align-items-center">
                <TimePart value={ hours }
                          placeholder="HH"
                          min={ 0 }
                          max={ 24 }
                          onChange={ handleHourChange } />
                :
                <TimePart value={ minutes }
                          placeholder="MM"
                          min={ 0 }
                          max={ 60 }
                          onChange={ handleMinuteChange } />
            </div>
        </div>
    )
}

function TimePart({ value, placeholder, onChange, min, max }) {
    function handleChange(newValueString) {
        if (newValueString !== '') {
            if (isNaN(newValueString)) {
                onChange('');
                return;
            }

            const newValue = parseInt(newValueString);

            if (newValue < max && newValue > min) {
                onChange(newValue.toString())
            }
        } else {
            onChange('');
        }
    }

    return (
        <input className="border py-1 px-2 text-center rounded-1"
               value={ value && value !== '' && parseInt(value) }
               placeholder={ placeholder }
               onChange={ e => handleChange(e.target.value) }
               size={ 3 }
               min={ min }
               max={ max } />
    )
}
