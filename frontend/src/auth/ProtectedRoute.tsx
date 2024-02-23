import {isUserAuthenticated} from "./components/AuthService";
import {Bounce, toast} from "react-toastify";

//@ts-ignore
export const ProtectedRoute = ({ children }) => {
    const auth = isUserAuthenticated()
    const loginUrl = process.env.REACT_APP_BACKEND_URL + 'oauth2/authorization/okta'
    if (!auth){
        toast.info('It looks like your not authenticated or your session expired, let\'s log you back in!', {
            position: "top-center",
            autoClose: 5000,
            hideProgressBar: false,
            closeOnClick: true,
            pauseOnHover: true,
            draggable: false,
            progress: undefined,
            theme: "colored",
            transition: Bounce});
        return (window.location.href = loginUrl);
    }
    return children;
};