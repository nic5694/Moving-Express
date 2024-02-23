import React, { useEffect } from 'react'

function LogoutRedirect() {
    useEffect(() => {
        console.log(document.cookie)
        // @ts-ignore
        document.getElementById('submit').click()
    }, [])

    return (
        <div className={'logoutRedirect'}>
            <div className="loader">
                <p>Creating account...</p>
            </div>

            <form
                method={'post'}
                action={
                    process.env.REACT_APP_BACKEND_URL + 'api/v1/movingexpress/logout?isLogoutSignUp=true'
                }
                id="logoutForm"
            >
                <button
                    id={'submit'}
                    type={'submit'}
                    style={{
                        display: 'none',
                        visibility: 'hidden',
                    }}
                >
                    Logout
                </button>
            </form>
        </div>
    )
}

export default LogoutRedirect
