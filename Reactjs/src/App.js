
import './App.css';
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import { connect } from 'react-redux'
import Register from './Authentication/Register';
import Login from './Authentication/Login';
import React from 'react';
import FrontPage from './FrontPage/FrontPage'
import Navbar from './Navbar/Navbar';
import CreateCommunity from './Community/CreateCommunity';
import ViewCommunity from './Community/ViewCommunity';
import CreatePost from './Post/CreatePost';
import ViewPost from './Post/ViewPost';
import ViewCommunityPostsMemberOf from './Community/ViewCommunityPostsMemberOf';
import SetWallpaper from './Community/SetWallpaper';
import SetLogo from './Community/SetLogo';
import ProfilePage from './Profile/ProfilePage';
import SetProfileImage from './Profile/SetProfileImage';
import SetPostImage from './Post/SetPostImage';
import SetCommentImage from './Comment/SetCommentImage';
import NavbarLoggedIn from './Navbar/NavbarLoggedIn';
import NavbarLoggedout from './Navbar/NavbarLoggedout';
import * as auth from "./Services/auth-header"
import ViewCommunitiesOfUser from './Community/ViewCommunitiesOfUser';
import EditPost from './Post/EditPost';
import EditComment from './Comment/EditComment';
import ChangePrivlegdeSettings from './Community/ChangePrivlegdeSettings';
import CommunitySettings from './Community/CommunitySettings';
import PostsOfUser from './Profile/PostsOfUser';
import CommentsOfUser from './Profile/CommentsOfUser';
import RequestsToJoinCommunity from './Community/RequestsToJoinCommunity';
import Chat from './Chat/Chat';
import store from './Slices';
import SetWallpaperProfile from './Profile/SetWallpaperProfile';
import MembersOfCommunity from './Community/MembersOfCommunity';
import ViewModPrivleges from './Community/ViewModPrivleges';
import ViewAllPublicCommunityPosts from './Community/ViewAllPublicCommunityPosts';





class App extends React.Component {
  constructor(props) {
    super(props);
    
    
    this.state = {
      isAutherized: false,
    }
    


    
  }






  checkIfAuthorized() {
    console.log("Updated State:", store.getState().user);
    


    let token = localStorage.getItem("token")

    if (token !== null) //det finnes en token
    {
      (async () => {
        const tokenStatus = await auth.checkValidToken(localStorage.getItem("token"));
          if (tokenStatus !== 200) //token er utløpt
          {this.setState({isAutherized: false})
          localStorage.setItem('token', "non")
        }
          else if (tokenStatus === 200) //token er valid
          {this.setState({isAutherized: true})}
      })()
        
    }
  }





  componentDidMount() {
    this.checkIfAuthorized()
  }




  render() {
    const { isAutherized } = this.state;





  return (
    <>




      



    
      
      <Router>



      {isAutherized ? <NavbarLoggedIn /> : <NavbarLoggedout/>}





        <Routes>
        <Route path='/' element={
            <>
            <ViewAllPublicCommunityPosts></ViewAllPublicCommunityPosts>
            </> 
          }/>

          <Route path='/register' element={
            <>
            <Register></Register>
            </> 
          }/>

          <Route path='/login' element={
            <>
            <Login></Login>
            </> 
          }/>
          <Route path='/create_community' element={
            <>
            <CreateCommunity></CreateCommunity>
            </> 
          }/>







          <Route path='/chat/:chatId/:user' element={
            <>
            <Chat/>
            </>
          }/>
          <Route path='/community/:community_name' element={
            <>
            <ViewCommunity/>
            </>
          }/>
            <Route path='/community/:community_name/submit' element={
            <>
            <CreatePost/>
            </>
          }/>
            <Route path='/community/:community_name/:postId' element={
            <>
            <ViewPost/>
            </>
          }/>
          <Route path='/:postId/set_image' element={
            <>
            <SetPostImage/>
            </>
          }/>
          <Route path='/:postId/set_comment_image/:commentId' element={
            <>
            <SetCommentImage/>
            </>
          }/>
          <Route path='/:postId/edit_comment/:commentId' element={
            <>
            <EditComment/>
            </>
          }/>

          <Route path=':community_name/edit_post/:postId' element={
            <>
            <EditPost/>
            </>
          }/>

            <Route path='/community/:community_name/set_wallpaper' element={
            <>
            <SetWallpaper/>
            </>
          }/>
          <Route path='/community/:community_name/requestsToJoin' element={
            <>
            <RequestsToJoinCommunity/>
            </>
          }/>



            <Route path='/community/:community_name/set_logo' element={
            <>
            <SetLogo/>
            </>
          }/>
            <Route path='/profile/:username/set_wallpaper' element={
            <>
            <SetWallpaperProfile/>
            </>
          }/>
          <Route path='/profile/:username' element={
            <>
            <ProfilePage/>
            </>
          }/>
          <Route path='/profile/:username/posts' element={
            <>
            <PostsOfUser/>
            </>
          }/>
            <Route path='/profile/:username/comments' element={
            <>
            <CommentsOfUser/>
            </>
          }/>
            <Route path='/profile/:username/communities' element={
            <>
            <ViewCommunitiesOfUser/>
            </>
          }/>
          <Route path='/community/:community_name/mod_privleges' element={
            <>
            <ViewModPrivleges/>
            </>
          }/>
            <Route path='/profile/:username/set_profile_image' element={
            <>
            <SetProfileImage/>
            </>
          }/>
            <Route path='/community/:community_name/change_privleges' element={
            <>
            <ChangePrivlegdeSettings/>
            </>
          }/>
          <Route path='/community/:community_name/members' element={
            <>
            <MembersOfCommunity/>
            </>
          }/>
            <Route path='/community/:community_name/settings' element={
            <>
            <CommunitySettings/>
            </>
          }/>


          




        </Routes>

      </Router>

      {/* 
      <Footer />
        */}
    
    </>
  );
      }
}

export default connect()(App);
