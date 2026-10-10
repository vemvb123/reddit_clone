import React from 'react'
import { useState } from 'react';
import * as communityService from "../Services/CommunityService"


export default function CreateCommunity() {
    const [communityData, setCommunityData] = useState({
      title: '',
      description: '',
      communityType: 'PUBLIC'
    });
  
    const handleCommunityTypeChange = (communityType) => {
      setCommunityData(prevData => ({ ...prevData, communityType: communityType }));
    };
  
    const handleCreateCommunity = async (e) => {
      e.preventDefault();
      try {
          const response = await communityService.saveCommunity(communityData);
          if (response.status === 200) {
              const slug = response.data.title.replace(" ", "-")
              window.location.href = "/community/" + slug
          }
    } catch (error) {
        console.error("Error: ", error)
      }
};

    const handleCommunityTitleChange = (e) => {
      const inputText = e.target.value.slice(0, 21); // Limit to 21 characters
      setCommunityData((prevData) => ({ ...prevData, title: inputText }));
    };
    

  
    return (
      <div className="container mt-4">
        <h2>Create a Community</h2>
  
      {/* Community Title */}
      <div className="mb-3">
        <label htmlFor="communityTitle" className="form-label">
          Community Title
        </label>
        <input
          type="text"
          className="form-control"
          id="communityTitle"
          value={communityData.title} 
          onChange={handleCommunityTitleChange}
        />
        <small className="form-text text-muted">{21 - communityData.title.length} characters remaining</small>
      </div>
  
        {/* Community Description */}
        <div className="mb-3">
          <label htmlFor="communityDescription" className="form-label">
            Description
          </label>
          <textarea
            className="form-control"
            id="communityDescription"
            rows="3"
            value={communityData.description}
            onChange={(e) => setCommunityData(prevData => ({ ...prevData, description: e.target.value }))}
          />
        </div>
  
        {/* Community Type */}
        <div className="mb-3">
          <label className="form-label">Community Type</label>
          <div className="form-check">
            <div className="form-check-inline">
              <input
                className="form-check-input"
                type="radio"
                name="communityType"
                id="publicOption"
                value="Public"
                checked={communityData.communityType === 'PUBLIC'}
                onChange={() => handleCommunityTypeChange('PUBLIC')}
              />
              <label className="form-check-label" htmlFor="publicOption">
                Public
              </label>
              <small className="form-text text-muted"> Anyone can view, post, and comment to this community</small>
            </div>
          </div>
          <div className="form-check">
            <div className="form-check-inline">
              <input
                className="form-check-input"
                type="radio"
                name="communityType"
                id="restrictedOption"
                value="Restricted"
                checked={communityData.communityType === 'RESTRICTED'}
                onChange={() => handleCommunityTypeChange('RESTRICTED')}
              />
              <label className="form-check-label" htmlFor="restrictedOption">
                Restricted
              </label>
              <small className="form-text text-muted"> Anyone can view this community, but only approved users can post</small>
            </div>
          </div>
          <div className="form-check">
            <div className="form-check-inline">
              <input
                className="form-check-input"
                type="radio"
                name="communityType"
                id="privateOption"
                value="Private"
                checked={communityData.communityType === 'PRIVATE'}
                onChange={() => handleCommunityTypeChange('PRIVATE')}
              />
              <label className="form-check-label" htmlFor="privateOption">
                Private
              </label>
              <small className="form-text text-muted"> Only approved users can view and submit to this community</small>
            </div>
          </div>
        </div>
  
        {/* Create Community Button */}
        <button
          type="button"
          className="btn btn-primary"
          onClick={handleCreateCommunity}
          disabled={!communityData.title || !communityData.description || !communityData.communityType}
        >
          Create Community
        </button>
      </div>
    );
  }