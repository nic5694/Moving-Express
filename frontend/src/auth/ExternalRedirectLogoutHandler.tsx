import React, { useEffect } from 'react'

function ExternalRedirectLogoutHandler() {
    useEffect(() => {
        // @ts-ignore
        console.log(document.cookie)
        // @ts-ignore
        if (document.cookie.includes('isAuthenticated=true')) {
            // @ts-ignore
            document.getElementById('submit').click()
        } else {
            window.location.href =
                process.env.REACT_APP_BACKEND_URL + 'oauth2/authorization/okta'
        }
    }, [])

    return (
        <div className={'logoutRedirect'}>
            <form
                method={'post'}
                action={
                    process.env.REACT_APP_BACKEND_URL + 'api/v1/movingexpress/logout?isLogoutExternal=true'
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

export default ExternalRedirectLogoutHandler
