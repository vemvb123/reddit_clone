import 'bootstrap/dist/css/bootstrap.css';
import React, { useState } from 'react';
import { Form, Button } from 'react-bootstrap';
import axios from 'axios';
import { useParams } from 'react-router-dom';
import * as postService from "../Services/PostService"

export default function SetPostImage() {

    let params = useParams()
    let postId = params.postId

    const [selectedFile, setSelectedFile] = useState(null);

    const handleFileInput = (e) => {
      e.preventDefault();
      setSelectedFile(e.target.files[0]);
  
        const reader = new FileReader();
  
        reader.onloadend = () => {
          setPreview(reader.result);
        };
  
        reader.readAsDataURL(e.target.files[0]);
      
    };

    const [preview, setPreview] = useState(null);

    const showFilePreview = () => {
      if (selectedFile) {
        const reader = new FileReader();
  
        reader.onloadend = () => {
          setPreview(reader.result);
        };
  
        reader.readAsDataURL(selectedFile);
      }
    };
  

    const handleUpload = () => {

        
        const token = localStorage.getItem('token');
        postService.setImage(token, selectedFile, postId)


      };


      


  return (




    <>
      <Form.Group className="mb-5">
        <Form.Label>Upload file</Form.Label>
        <Form.Control onChange={handleFileInput} type="file" className='w-75 mx-auto mb-2' />
        <Button className='mt-3' onClick={handleUpload}>Upload file</Button>
        {preview && <img src={preview} alt="File Preview" style={{ maxWidth: '100%' }} />}
      </Form.Group>
    </>










  )
}
