import React from 'react'
import { Container } from 'react-bootstrap'

import { useEffect } from 'react'
import {  Button, Card } from 'react-bootstrap';
import { Comment } from './Comment';

export const Comments = (props) => {


  useEffect(() => {
    console.log("heraaaa")
    console.log(props.comments)
    console.log(props)

  }, [props.comments])
  

  return (
    <Container>

        

        {props.comments.map((comment) => {
            return <Comment 
                id = {comment.id}
                title = {comment.title}
                description = {comment.description}
                username = {comment.username}
                postId = {comment.postId}
                parentCommentId = {comment.parentCommentId}
                hasChildren = {comment.hasChildren}
                isLastChild = {comment.isLastChild}
                createdAt = {comment.createdAt}
                pathToImage = {comment.pathToImage}
                pathToUserImage = {comment.pathToUserImage}
                
                toggleShowMoreCommentsButton = {props.toggleShowMoreCommentsButton}
            />
        })}






    </Container>
  )
}




