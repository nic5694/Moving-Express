import './App.css'
import {BrowserRouter as Router, Route, Routes, BrowserRouter, RouterProvider} from 'react-router-dom'
import React from 'react'

import LandingPage from './Pages/LandingPage'
import ShipmentQuotePage from './Pages/ShipmentQuotePage'
import Footer from './Components/Footer'
import UserHomePage from './Pages/UserHomePage'
import ShipmentsPage from './Pages/ShipmentsPage'
import EstimatorPage from './Pages/ShipmentEstimatorPage'
import ReviewerPage from './Pages/ShipmentReviewerPage'
import LogoutRedirectHandler from './auth/LogoutRedirectHandler'
import {AuthProvider} from './auth/components/AuthService'
import UserShipmentQuotePage from './Pages/UserShipmentQuotePage'
import UserContactUsPage from './Pages/UserContactUsPage'
import ShipmentDetailsPage from './Pages/ShipmentDetailsPage'
import UserProfilePage from './Pages/UserProfilePage'
import ObserverShipmentDetails from './Pages/ObserverShipmentDetails';
import {ProtectedRoute} from "./auth/ProtectedRoute";
import NotFoundPage from "./auth/components/NotFoundPage";
import DriverPage from './Pages/DriverPage'

function App() {
    return (
        <>
            <AuthProvider>
                <BrowserRouter>
                    <Routes>
                        <Route path="/" element={<LandingPage />} />
                        <Route path="/ShipmentQuote" element={<ShipmentQuotePage />} />
                        <Route path="/UserShipmentQuote" element={<ProtectedRoute><UserShipmentQuotePage /></ProtectedRoute>} />
                        <Route path="/Home" element={<ProtectedRoute><UserHomePage /></ProtectedRoute>} />
                        <Route path="/Shipments" element={<ProtectedRoute><ShipmentsPage /></ProtectedRoute>} />
                        <Route path="/Estimator" element={<ProtectedRoute><EstimatorPage /></ProtectedRoute>} />
                        <Route path="/Reviewer" element={<ProtectedRoute><ReviewerPage /></ProtectedRoute>} />
                        <Route path="/logout" element={<LogoutRedirectHandler />} />
                        <Route path="/ContactUs" element={<ProtectedRoute><UserContactUsPage /></ProtectedRoute>} />
                        <Route path="/Profile" element={<ProtectedRoute><UserProfilePage /></ProtectedRoute>} />
                        <Route path="/ShipmentDetails" element={<ProtectedRoute><ShipmentDetailsPage /></ProtectedRoute>} />
                        <Route path="/ObserverShipmentDetails/:observerCode" element={<ObserverShipmentDetails />} />
                        <Route path="/Driver" element={<ProtectedRoute><DriverPage/></ProtectedRoute>} />
                        <Route path="*" element={<NotFoundPage />} />
                    </Routes>
                    <Footer/>
                </BrowserRouter>
            </AuthProvider>
        </>
    )
}

export default App