import React, { useEffect, useRef, useReducer } from "react";
import "./RegistrationAuth.css";

function doSubmit(submittedValues) {
    console.log(`Submitted: ${submittedValues.join("")}`);

    return new Promise((resolve) => {
        setTimeout(() => {
            resolve();
        }, 1500);
    });
}

function clampIndex(index) {
    if (index > 6) {
        return 6;
    } else if (index < 0) {
        return 0;
    } else {
        return index;
    }
}

function reducer(state, action) {
    switch (action.type) {
        case "INPUT":
            return {
                ...state,
                inputValues: [
                    ...state.inputValues.slice(0, action.payload.index),
                    action.payload.value,
                    ...state.inputValues.slice(action.payload.index + 1)
                ],
                focusedIndex: clampIndex(state.focusedIndex + 1)
            };

        case "BACK":
            return {
                ...state,
                focusedIndex: clampIndex(state.focusedIndex - 1)
            };

        case "PASTE":
            return {
                ...state,
                inputValues: state.inputValues.map(
                    (_, index) => action.payload.pastedValue[index] || ""
                )
            };

        case "FOCUS":
            return {
                ...state,
                focusedIndex: action.payload.focusedIndex
            };

        case "VERIFY":
            return {
                ...state,
                status: "pending"
            };

        case "VERIFY_SUCCESS":
            return {
                ...state,
                status: "idle"
            };

        default:
            throw new Error("unknown action");
    }
}

const initialState = {
    inputValues: Array(6).fill(""),
    focusedIndex: 0,
    status: "idle"
};

export default function App() {
    const [{ inputValues, focusedIndex, status }, dispatch] = useReducer(
        reducer,
        initialState
    );

    function handleInput(index, value) {
        dispatch({ type: "INPUT", payload: { index, value } });
    }

    function handleBack() {
        dispatch({ type: "BACK" });
    }

    function handlePaste(pastedValue) {
        dispatch({ type: "PASTE", payload: { pastedValue } });

        if (pastedValue.length === 6) {
            dispatch({ type: "VERIFY" });
            doSubmit(pastedValue.split("")).then(() =>
                dispatch({ type: "VERIFY_SUCCESS" })
            );
        }
    }

    function handleFocus(focusedIndex) {
        dispatch({ type: "FOCUS", payload: { focusedIndex } });
    }

    function handleSubmit(e) {
        e.preventDefault();

        dispatch({ type: "VERIFY" });
        doSubmit(inputValues).then(() => dispatch({ type: "VERIFY_SUCCESS" }));
    }

    //dummy data
    const baseImage = 'data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAV4AAAFeAQAAAADlUEq3AAADHklEQVR4Xu2ZO3YiMRBFi+OA0EtgKV5as7ReCktw6MAHTd1XAhfuNuOJ51WAkPqqg0f9JGL83j7j+8oTM9zNcDfD3Qx3+z/ga8jy2zvD5zG/nTWsufihhy+Gd2G+jTN7Tmuuf7zmLIecLVfNCjG8A6eWbwmj7IKyJ3YnJZ1fLxGGn8L57T11TrlfL28Dp0zqYPgX8HRRdJYd2Wr4L3B+EN3H99wDd6pYzxnwDTG8hVlHy5Q05f4+6KHhXXgaOqfA+YSMeI/uLzO8gSd1UGJcKC2ZGKumLNIZTzW8B2e/l/AgFcbNlnxUcmsPlOEtrKzHupyylNXTIYel0LQfxXCHyyk1BE6JsofPwDeni/YsavgLxlJgtcr3YjJdVMP8KQzvwPk4Ull1MikwbZ/W16gup8zwPpxuyJkjP6gw60JGRHUV56W7qOEe3TqnlcBVjmO5V5gjb+uJ0XCvKdyoxBWBR+XAuqG6BIt4quGf4JFhPbiTCjLirDCIr1g3/ATmTkrFJAMZ+DZokd+gRbfhB5hyTFhnPC+aZUZ80X3VAdWz3gzDe3B+ls6D6G4uis6ZGk+zKhvewOWGqeUc2MMihWYQ3XqR4X14SqpWWcouUPgmQ78rMPwA81iTKh9ZWsrSYTV76zobfvC6kckPUxPITH9kkCZZ1BsM78JxpACrfNza6CrAyoh1EDH8A8wpQ/lRDkuso2xuNfwcfqn12hPy1NBxbcVh6a1lhrdw6LimPbqoylSoNwCzZxDrhndgLHWeTonEpzXqmmWwVTPDe3D1e2qVQxdVWB3eQovxEN2Gu85yytnvDa5Hg26aOjx0LCkzvIHVP2v93gQS3TTVctgU3/A+PI0cmDoT1lmOqSmX8s0LWw3vwRXBdDIoqwpDWKslVORHvdLwFkZE3a9QlbmGP2kPq6XzuWdRwx0mnoF5POY1S7loFZruooa38JVTBqnwqDqMw67LmP9K8g7DP8GDysvjdMqotk+X8vyTu+OihgUDnGdGPGhP1Fb2IH7X2XCDQ3bVJUHNFOtcEuhFa3dRww3+rRnuZrib4W6Guxnu9m/wHxi6atlh67DeAAAAAElFTkSuQmCC'
    
    return (
        <div id="auth-container">
            <form id="authForm" onSubmit={handleSubmit}>
                <div className="QRCodeContainer">
                    <h1>Scan QR code</h1>
                    <img src={baseImage} height='300px'></img>
                </div>
                <div className="inputs">
                    {inputValues.map((value, index) => {
                        return (
                            <Input
                                key={index}
                                index={index}
                                value={value}
                                onChange={handleInput}
                                onBackspace={handleBack}
                                onPaste={handlePaste}
                                isFocused={index === focusedIndex}
                                onFocus={handleFocus}
                                isDisabled={status === "pending"}
                            />
                        );
                    })}
                </div>
                <button disabled={status === "pending"}>
                    {status === "pending" ? "VERIFYING..." : "VERIFY"}
                </button>
            </form>
        </div>
    );
}

function Input({
                   index,
                   value,
                   onChange,
                   onPaste,
                   onBackspace,
                   isFocused,
                   onFocus,
                   isDisabled
               }) {
    const ref = useRef();
    useEffect(() => {
        requestAnimationFrame(() => {
            if (ref.current !== document.activeElement && isFocused) {
                ref.current.focus();
            }
        });
    }, [isFocused]);

    function handleChange(e) {
        onChange(index, e.target.value);
    }

    function handlePaste(e) {
        onPaste(e.clipboardData.getData("text"));
    }

    function handleKeyDown(e) {
        if (e.key === "Backspace") {
            onBackspace();
        }
    }

    function handleFocus(e) {
        e.target.setSelectionRange(0, 1);
        onFocus(index);
    }

    return (
        <input
            ref={ref}
            type="text"
            value={value}
            onChange={handleChange}
            onPaste={handlePaste}
            onKeyDown={handleKeyDown}
            maxLength="1"
            onFocus={handleFocus}
            disabled={isDisabled}
        />
    );
}
