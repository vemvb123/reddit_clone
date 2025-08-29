import React from 'react'
import { Button, Card, Collapse, Container, } from 'react-bootstrap';
import { CreateComment } from './CreateComment';
import { useState } from 'react';
import { useEffect } from 'react';
import * as commentService from "../Services/CommentService"
import { Comments } from './Comments';
import RoundImage from '../Community/RoundImage';




export const Comment = (props) => {


useEffect(() => {
  if (props.isLastChild === true && props.parentCommentId === null)
  {
    props.toggleShowMoreCommentsButton()
  }
  console.log("kommen")
  console.log(props)
}, [])




//State for å vise knapp for å lage reply
const [showCreateReply, setshowCreateReply] = useState(false);
const toggleshowCreateReply = () => setshowCreateReply(!showCreateReply);

//Replies
const [replies, setReplies] = useState([])
const toggleShowReplies = () => setshowCreateReply(!toggleShowReplies)

//State for om siste reply til kommentaren er funnet
 const [lastReplyFound, setLastReplyFound] = useState(false)
 
//State for høyeste ID
 const [highestId, setHighestId] = useState(0)

//om knapp for å vise flere replies skal si "Show replies" eller "Show more replies"
const [showRepliesOrShowMore, setShowRepliesOrShowMore] = useState(true)

//Om man skal vise replies
const [showReplies, setShowReplies] = useState(true)



 //Funksjon for å hente flere kommentarer
 async function getIntervallOfComments() {
    if (replies.length === 0 || !showRepliesOrShowMore) 
    {
        let token = localStorage.getItem('token');

      //får resultatet
      let result = await commentService.getIntervallOfComments(token, props.postId, highestId, props.id)
      result = result.filter(reply => !replies.includes(reply))
      setReplies([...replies, ...result])
      console.log("her er unga")
      console.log(result)

      //ser om resultatet inneholder den siste ungen til kommentaren
      if (result[result.length - 1].isLastChild) 
          {setLastReplyFound(true)}

      //setter høyeste id funnet
      for (let i = 0; i < result.length; i++) 
        {if (result[i].id > highestId) {
            setHighestId(result[i].id)
          }}
    }

 }

 

 let defaultImageUrl = "https://i.guim.co.uk/img/media/dd703cd39013271a45bc199fae6aa1ddad72faaf/0_0_2000_1200/master/2000.jpg?width=1200&height=900&quality=85&auto=format&fit=crop&s=2192262d7832a184dbb583c238563695"


  return (
    <Container className='w-75 mb-5'>

        {/* Viser kommentaren */}
        <Card>
            <Card.Body>
                <Card.Img src={`http://localhost:8080/image/get_file_by_filename/${props.pathToImage}`}></Card.Img>
                <Card.Title>{props.username}</Card.Title>


                <RoundImage imageUrl={`http://localhost:8080/image/get_file_by_filename/${props.pathToUserImage}`} to={`/profile/${props.username}`} sizeInPixels={"50px"} text={"See profile"}/>
                
                
                
                




                <Card.Title>{props.title}</Card.Title>
                <Card.Text className='border p-2'>
                    {props.description}
                </Card.Text>
                <Card.Text>{props.createdAt}</Card.Text>
                
            <Button variant="link" className="text-muted" href={`/${props.postId}/edit_comment/${props.id}`}>
                    Edit comment
            </Button>
            </Card.Body>







            {/* Knapp for å lage reply */}
            <Button className='w-25' onClick={toggleshowCreateReply}>
            {showCreateReply ? 'Hide create Reply' : 'Show create reply'}
            </Button>
            <Collapse in={showCreateReply}>
              <div>
                <CreateComment postId={props.postId} replyTo={props.id}/>
              </div>
            </Collapse>




              {/* Replies */}
              <Collapse in={showReplies}>
                <div>
                <Comments comments={replies}></Comments>
                </div>
              </Collapse>




              {/* Knapp for å vise replies */}
              <Collapse in={(props.hasChildren && !lastReplyFound) || (props.hasChildren && showRepliesOrShowMore)}>
                <div>
                  <Button onClick={event => {
                    getIntervallOfComments();
                    setShowRepliesOrShowMore(false);
                    setShowReplies(true)
                    }}>
                    {showRepliesOrShowMore ? 'Show replies' : 'Show more replies'}
                  </Button>
                </div>              
              </Collapse>




              {/* Knapp for å gjemme replies */}
              <Collapse in={!showRepliesOrShowMore}>
                <div>
                  <Button onClick={event => {
                    setShowReplies(false);
                    setShowRepliesOrShowMore(true)
                    
                  }}>
                    Hide replies
                  </Button>
                </div>
              </Collapse>


                  



              
              

        </Card>
    </Container>
    
  )
}
