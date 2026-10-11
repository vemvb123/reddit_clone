
import './App.css';
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import { connect } from 'react-redux'
import Register from './Page/Authentication/Register';
import Login from './Page/Authentication/Login';
import React from 'react';
import FrontPage from './Page/FrontPage/FrontPage'
import Navbar from './Component/Navbar/Navbar';
import CreateCommunity from './Page/Community/CreateCommunity';
import ViewCommunity from './Page/Community/ViewCommunity';
import CreatePost from './Page/Post/CreatePost';
import ViewPost from './Component/Post/ViewPost';
import ViewCommunityPostsMemberOf from './Page/Community/ViewCommunityPostsMemberOf';
import SetWallpaper from './Page/Community/SetWallpaper';
import SetLogo from './Page/Community/SetLogo';
import ProfilePage from './Page/Profile/ProfilePage';
import SetProfileImage from './Component/Profile/SetProfileImage';
import SetPostImage from './Page/Post/SetPostImage';
import SetCommentImage from './Page/Comment/SetCommentImage';
import NavbarLoggedIn from './Component/Navbar/NavbarLoggedIn';
import NavbarLoggedout from './Component/Navbar/NavbarLoggedout';
import * as auth from "./Service/auth-header"
import ViewCommunitiesOfUser from './Page/Community/ViewCommunitiesOfUser';
import EditPost from './Page/Post/EditPost';
import EditComment from './Page/Comment/EditComment';
import ChangePrivlegdeSettings from './Page/Community/ChangePrivlegdeSettings';
import CommunitySettings from './Page/Community/CommunitySettings';
import PostsOfUser from './Component/Profile/PostsOfUser';
import CommentsOfUser from './Component/Profile/CommentsOfUser';
import RequestsToJoinCommunity from './Page/Community/RequestsToJoinCommunity';
import Chat from './Page/Chat/Chat';
import store from './Slice';
import SetWallpaperProfile from './Component/Profile/SetWallpaperProfile';
import MembersOfCommunity from './Page/Community/MembersOfCommunity';
import ViewModPrivleges from './Page/Community/ViewModPrivleges';
import ViewAllPublicCommunityPosts from './Page/Community/ViewAllPublicCommunityPosts';


class App extends React.Component {
  constructor(props) {
    super(props);

    this.state = {
      isAutherized: false,
    }

  }

  checkIfAuthorized() {
    const token = localStorage.getItem("token");

    if (!token) {
      this.setState({ isAutherized: false });
      return;
    }

    (async () => {
      const tokenStatus = await auth.checkValidToken(token);

      if (tokenStatus === 200) {
        this.setState({ isAutherized: true });
      } else {
        localStorage.removeItem("token");
        this.setState({ isAutherized: false });
      }
      })();
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
