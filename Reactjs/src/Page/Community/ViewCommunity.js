import React, { useEffect, useState } from 'react'
import { useParams, Link } from 'react-router-dom'
import 'bootstrap/dist/css/bootstrap.min.css'; // Import Bootstrap styles
import { Container, Row, Button } from 'react-bootstrap';

import Wallpaper from '../../Component/Community/Wallpaper';
import * as communityService from "../../Service/CommunityService"
import * as postService from "../../Service/PostService"

import Posts from '../../Component/Post/Posts';
import ErrorMessage from "../../Component/SiteMessage/ErrorMessage";
import LoadingMessage from "../../Component/SiteMessage/LoadingMessage";


export default function ViewCommunity() {
    let params = useParams()
    const communityName = params.community_name;

    const [community, setCommunity] = useState(null)
    const [role, setRole] = useState(null)
    const [posts, setPosts] = useState([])
    const [page, setPage] = useState(0);
    const [loading, setLoading] = useState(true)
    const [error, setError] = useState(null)

    useEffect(() => {
        const fetchData = async () => {
            try {
                const communityResponse = await communityService.getCommunity(communityName);
                communityResponse.error ? setError(communityResponse.error.message) : setCommunity(communityResponse.data);

                const isMemberResponse = await communityService.getUserIsHasRoleInCommunityOrIsNotAMember(communityName)
                isMemberResponse.error ? setError(isMemberResponse.error.message) : setRole(isMemberResponse.data.role);

                const postResponse = await postService.getLatestPosts(communityName, page)
                postResponse.error ? setError(postResponse.error.message) : setPosts(postResponse.data);

                setLoading(false);

            } catch (error) {
                console.error('Error fetching posts:', error);
            }
        };
        fetchData();
    }, [page]);


    const handleShowMoreClick = () => {
        setPage((prevPage) => prevPage + 1);
    };

    const becomeMember = async (e) => {
        const response = await communityService.makeUserBecomeMemberOfCommunity(communityName)
        if (response.error) {
            setError(response.error)
        }
    };

    const requestToJoinCommunity = async (e) => {
        const response = communityService.requestToJoinCommunity(communityName)
        if (response.error) {
            setError(response.error)
        }
    };

    const unsubscribeFromCommunity = async (e) => {
        e.preventDefault();
        const response = await communityService.unsubscribeFromCommunity(communityName);
        response.error ? setError(response.error) : window.location.href = "/";
    }


    const noHoverBgStyle = `
        .no-hover-bg:hover {
            background-color: transparent !important;
        }
    `;


    if (loading) {
        <LoadingMessage/>
    }
    if (error) {
        <ErrorMessage message={error}/>
    }

    return (
        <div>
            <style>{noHoverBgStyle}</style>

            <Wallpaper
                  imageUrl={community?.communityWallpaper ? `http://localhost:8080/image/${community?.communityWallpaper}` : null}
                  existingLink={`/community/${communityName}`}
                  role={role}
            />

            <div style={{ textAlign: 'center', margin: '10px' }}>
                {/* TODO: Make buttons seem more like links and show them based on user's role */}
                {["ADMIN", "MOD"].includes(role) && (
                    <>
                        <Link to={`/community/${community.title}/settings`} className="btn btn-link no-hover-bg">Community settings</Link>
                    </>
                )}


                {["ADMIN"].includes(role) && (
                    <>
                        <Link to={`/community/${community.title}/change_privleges`} className="btn btn-link no-hover-bg">Change privilege settings</Link>
                    </>
                )}

                {/* TODO: Centered buttons above ViewPosts */}
                {["ADMIN", "MOD", "MEMBER"].includes(role) && (
                    <>
                        <Button href={`/community/${communityName}/submit`} className="btn btn-link no-hover-bg">Make post</Button>
                        <Button onClick={unsubscribeFromCommunity} className="btn btn-link no-hover-bg">Unsubscribe</Button>
                    </>
                )}

                {/* TODO: Show RequestS to join community button based on role and community type */}
                {(role === "NOT_MEMBER") && (community.communityType === "PRIVATE" || community.communityType === "RESTRICTED") && (
                    <Button onClick={requestToJoinCommunity} className="btn btn-link no-hover-bg">Request to join community</Button>
                )}

                {/* TODO: Show RequestS to join community button for PUBLIC community */}
                {(role === "NOT_MEMBER") && (community.communityType === "PUBLIC") && (
                    <>
                        <Button href={`/community/${communityName}/submit`} className="btn btn-link no-hover-bg">Make post</Button>
                    </>
                )}

                {(role === "NOT_MEMBER") && (community.communityType !== "PUBLIC") && (
                    <>
                        <Button href={`/community/${communityName}/requestsToJoin`} className="btn btn-link no-hover-bg">Request to join community</Button>
                    </>
                )}

                <Button href={`/community/${communityName}/members`} className="btn btn-link no-hover-bg">See members</Button>

                {["MOD"].includes(role) && (
                    <>
                        <Button href={`/community/${communityName}/mod_privleges`} className="btn btn-link no-hover-bg">Your mod privleges</Button>
                    </>
                )}

                {/* TODO: Show Become a member button for NOT_MEMBER */}
                {(role === "NOT_MEMBER") && (
                    <Button onClick={becomeMember} className="btn btn-link no-hover-bg">Become a member</Button>
                )}
            </div>


            <div style={{ display: 'flex', flexDirection: 'column', alignItems: 'center', justifyContent: 'center', gap: '5px', marginTop: '20px', marginBottom: '20px' }}>
                {/* ... (existing code) */}
            </div>

            <Container className=''>

            <Posts posts={posts}/>

            {posts?.length === 10 && (
            <Row className="justify-content-center mt-3">
                <Button onClick={handleShowMoreClick}>Show more posts</Button>
            </Row>
            )}

            </Container>

        </div>
    )

}
