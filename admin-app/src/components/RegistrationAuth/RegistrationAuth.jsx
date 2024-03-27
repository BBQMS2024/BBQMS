import React, { useEffect, useRef, useReducer, useState } from "react";
import "./RegistrationAuth.css";
import { SERVER_URL } from "../../constants";
import {useNavigate} from "react-router-dom";

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
        case "RESET_INPUTS":
            return {
                ...state,
                inputValues: Array(6).fill(""),
                focusedIndex: 0,
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

export default function RefistrationAuth( {qrCode, email} ) {
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
    let navigate = useNavigate();

    function routeChange() {
        let path = `/companydetails`;
        navigate(path);
    }
    const checkInputValues = async () =>{
        const enteredValues = inputValues.join("");
        const requestBody = {
            code: enteredValues,
            email: email
        }
        try{
            const response = await fetch(SERVER_URL + '/api/v1/auth/tfa', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify(requestBody)
            });
            if (response.ok) {
                dispatch({ type: "VERIFY_SUCCESS" });
                routeChange();
            } else {
                throw new Error("Code could not be verified. It is incorrect or expired.");
            }
        } catch (error) {
            alert("Code could not be verified. It is incorrect.");
            resetInputs();
        }
        
    }

    function resetInputs() {
        dispatch({ type: "RESET_INPUTS" });
    }

    return (
        <div className="auth-container-reg">
            <form className="authForm-reg" onSubmit={handleSubmit}>
                <div className="QRCodeContainer">
                    <h1>Scan QR code</h1>
                    <img src={qrCode} height='300px'></img>
                </div>
                <div className="inputs-reg">
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
                <button onClick={checkInputValues} disabled={status === "pending"}>
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
